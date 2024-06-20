package com.rezero.rotto.api.controller;

import com.rezero.rotto.api.service.FarmService;
import com.rezero.rotto.dto.response.FarmDetailResponse;
import com.rezero.rotto.dto.response.FarmListResponse;
import com.rezero.rotto.dto.response.FarmTop10ListResponse;
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
@RequestMapping("/farm")
@RequiredArgsConstructor
@Tag(name = "Farm 컨트롤러", description = "농장 관리를 위한 API")
public class FarmController {

    private final JwtTokenProvider jwtTokenProvider;
    private final FarmService farmService;

    @Operation(summary = "농장 목록 조회", description = "필터링, 정렬, 검색을 포함한 농장 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = FarmListResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
    })
    @GetMapping
    public ResponseEntity<?> getFarmList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                         @RequestParam(required = false) Integer page,
                                         @RequestParam(name = "is-liked", required = false) Boolean isLiked,
                                         @RequestParam(name = "subs-status", required = false) Integer subsStatus,
                                         @RequestParam(name = "min-price", required = false) Integer minPrice,
                                         @RequestParam(name = "max-price", required = false) Integer maxPrice,
                                         @RequestParam(name = "bean-type", required = false) String beanType,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(required = false) String keyword) {
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return farmService.getFarmList(userCode, page, isLiked, subsStatus, minPrice, maxPrice, beanType, sort, keyword);
    }


    @Operation(summary = "농장 상세 조회", description = "농장 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = FarmDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "사용자 혹은 농장이 존재하지 않음")
    })
    @GetMapping("/{farmCode}")
    public ResponseEntity<?> getFarmDetail(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable int farmCode) {
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return farmService.getFarmDetail(userCode, farmCode);
    }


    @Operation(summary = "수익률 Top 10 농장 목록 조회", description = "수익률 Top 10 농장 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = FarmTop10ListResponse.class))),
            @ApiResponse(responseCode = "404", description = "사용자가 존재하지 않음")
    })
    @GetMapping("/top-ten")
    public ResponseEntity<?> getRateTop10FarmList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return farmService.getRateTop10FarmList(userCode);
    }
}
