package com.rezero.rotto.api.controller;

import com.rezero.rotto.api.service.ApplyHistoryService;
import com.rezero.rotto.dto.response.ApplyHistoryListGetResponse;
import com.rezero.rotto.dto.response.ApplyHistoryResponse;
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
@RequestMapping("/apply")
@RequiredArgsConstructor
@Tag(name = "Apply 컨트롤러", description = "청약신청&청약신청 취소 기능을 위한 API")
public class ApplyHistoryController {

    private final ApplyHistoryService applyHistoryService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "청약신청",
            description = "청약신청")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200", description = "청약신청 성공",
                    content = @Content(schema = @Schema(implementation = ApplyHistoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않은 청약")
    })

    // 받을값들
    @PostMapping("/{subscriptionCode}")
    public ResponseEntity<?> postApply(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable int subscriptionCode, int applyCount){
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return applyHistoryService.postApply(userCode, subscriptionCode, applyCount);
    }


    @Operation(summary = "청약신청 취소",
            description = "청약신청 취소")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200", description = "청약신청 취소 성공",
                    content = @Content(schema = @Schema(implementation = ApplyHistoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않은 청약")
    })

    // 받을값들
    @PatchMapping("/{subscriptionCode}")
    public ResponseEntity<?> deleteApply(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable int subscriptionCode){
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return applyHistoryService.deleteApply(userCode, subscriptionCode);
    }


    @Operation(summary = "유저가 신청한 청약리스트",
            description = "유저가 신청한 청약리스트")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200", description = "유저의 청약리스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApplyHistoryListGetResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않은 청약리스트")
    })


    @GetMapping
    public ResponseEntity<?> getApply(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return applyHistoryService.getApply(userCode);
    }


    @Operation(summary = "유저가 신청취소한 청약리스트",
            description = "유저가 신청취소한 청약리스트")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200", description = "유저의 청약취소 리스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApplyHistoryListGetResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않은 청약리스트")
    })


    @GetMapping("/terminated")
    public ResponseEntity<?> getApplyTerminated(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return applyHistoryService.getApplyTerminated(userCode);
    }

}
