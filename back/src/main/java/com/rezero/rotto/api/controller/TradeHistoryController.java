package com.rezero.rotto.api.controller;

import com.rezero.rotto.api.service.TradeHistoryService;
import com.rezero.rotto.dto.response.SubscriptionListResponse;
import com.rezero.rotto.dto.response.TradeHistoryExpenseDetailOfSubResponse;
import com.rezero.rotto.dto.response.TradeHistoryHomeInfoResponse;
import com.rezero.rotto.dto.response.TradeHistoryListResponse;
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
@RequestMapping("/trade-history")
@Tag(name = "ApplyHistory 컨트롤러", description = "정산내역, 보유내역을 위한 API")
public class TradeHistoryController {

    private final TradeHistoryService tradeHistoryService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "정산내역 조회", description = "정산내역 목록을 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = TradeHistoryListResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
    })

    @GetMapping("/own")
    public ResponseEntity<?> getTradeHistoryOwn(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return tradeHistoryService.getTradeHistoryOwn(userCode);
    }


    @Operation(summary = "보유내역 조회", description = "보유내역 목록을 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = TradeHistoryListResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
    })

    @GetMapping
    public ResponseEntity<?> getTradeHistory(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return tradeHistoryService.getTradeHistory(userCode);
    }



    @Operation(summary = "정산내역 상세 조회", description = "정산내역 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = TradeHistoryExpenseDetailOfSubResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
    })

    @GetMapping("/own/{subscriptionCode}")
    public ResponseEntity<?> getExpenseDetailOfSub(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable int subscriptionCode) {
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return tradeHistoryService.getExpenseDetailOfSub(userCode, subscriptionCode);
    }


    @Operation(summary = "총 투자 내역 조회(홈)", description = "정산내역 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = TradeHistoryHomeInfoResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
    })

    @GetMapping("/home")
    public ResponseEntity<?> getHomeInvestInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return tradeHistoryService.getHomeInvestInfo(userCode);
    }
}
