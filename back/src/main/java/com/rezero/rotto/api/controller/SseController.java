package com.rezero.rotto.api.controller;

import com.rezero.rotto.api.service.SseService;
import com.rezero.rotto.utils.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
@Tag(name = "SSE 컨트롤러", description = "SSE 통신을 위한 컨트롤러")
public class SseController {

    private final SseService sseService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "SSE 연결 요청",
            description = "SSE 연결 요청")
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                              HttpServletResponse response) {
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        SseEmitter sseEmitter= sseService.subscribe(userCode);

        response.addHeader("X-Accel-Buffering", "no");
        return ResponseEntity.status(HttpStatus.OK).body(sseEmitter);
    }

    @Operation(summary = "SSE 연결 종료",
            description = "SSE 연결 종료")
    @DeleteMapping("/disconnect")
    public ResponseEntity<Void> disconnect(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        sseService.disConnect(userCode);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
