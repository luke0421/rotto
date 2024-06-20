package com.rezero.rotto.api.controller;

import com.rezero.rotto.api.service.BeanService;
import com.rezero.rotto.dto.response.BeanDetailResponse;
import com.rezero.rotto.dto.response.BeanListResponse;
import com.rezero.rotto.utils.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bean")
@RequiredArgsConstructor
@Tag(name = "Bean 컨트롤러", description = "원두 관리를 위한 API")
public class BeanController {

    private final BeanService beanService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "원두 목록 조회", description = "원두 목록을 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = BeanListResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
    })
    @GetMapping
    public ResponseEntity<?> getBeanList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return beanService.getBeanList(userCode);
    }


    @Operation(summary = "원두 상세 조회", description = "원두 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = BeanDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "사용자 혹은 원두가 존재하지 않음")
    })
    @GetMapping("/{beanCode}")
    public ResponseEntity<?> getBeanDetail(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable int beanCode) {
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return beanService.getBeanDetail(userCode, beanCode);
    }
}
