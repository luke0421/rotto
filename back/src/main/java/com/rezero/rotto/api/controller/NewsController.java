package com.rezero.rotto.api.controller;

import com.rezero.rotto.api.service.NewsService;
import com.rezero.rotto.dto.dto.NewsListDto;
import com.rezero.rotto.dto.response.NewsDetailResponse;
import com.rezero.rotto.dto.response.NewsListResponse;
import com.rezero.rotto.utils.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Tag(name = "News API", description = "서비스 관련 소식 관리를 위한 API")
public class NewsController {

    private final NewsService newsService;
    private final JwtTokenProvider jwtTokenProvider;


    @Operation(summary = "소식 목록 조회", description = "소식 목록을 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = NewsListResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
    })
    @GetMapping
    public ResponseEntity<?> getNewsList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                         @RequestParam(required = false) Integer page) {
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return newsService.getNewsList(userCode, page);
    }


    @Operation(summary = "소식 상세 조회", description = "소식 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = NewsDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 혹은 소식")
    })
    @GetMapping("/{newsCode}")
    public ResponseEntity<?> getNewsDetail(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                           @PathVariable int newsCode) {
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return newsService.getNewsDetail(userCode, newsCode);
    }

}
