package com.rezero.rotto.api.controller;

import com.rezero.rotto.api.service.NoticeService;
import com.rezero.rotto.dto.response.NoticeDetailResponse;
import com.rezero.rotto.dto.response.NoticeListResponse;
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
@RequiredArgsConstructor
@RequestMapping("/notice")
@Tag(name = "Notice 컨트롤러", description = "골지사항 조회를 위한 API")
public class NoticeController {

    private final NoticeService noticeService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "공지사항 목록 조회", description = "공지사항 목록을 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = NoticeListResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
    })
    @GetMapping
    public ResponseEntity<?> getNoticeList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return noticeService.getNoticeList(userCode);
    }


    @Operation(summary = "공지사항 상세 조회", description = "공지사항 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = NoticeDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 혹은 공지사항")
    })
    @GetMapping("/{noticeCode}")
    public ResponseEntity<?> getNoticeDetail(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable int noticeCode) {
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return noticeService.getNoticeDetail(userCode, noticeCode);
    }

}
