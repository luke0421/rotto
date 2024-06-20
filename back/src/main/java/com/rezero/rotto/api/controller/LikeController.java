package com.rezero.rotto.api.controller;

import com.rezero.rotto.api.service.LikeService;
import com.rezero.rotto.utils.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
@Tag(name = "Like 컨트롤러", description = "좋아요 기능 관리를 위한 API")
public class LikeController {

    private final JwtTokenProvider jwtTokenProvider;
    private final LikeService likeService;

    @Operation(summary = "관심 농장 등록", description = "해당 농장을 관심 농장으로 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 혹은 농장이 존재하지 않음"),
            @ApiResponse(responseCode = "409", description = "이미 관심 농장으로 등록되어 있음")
    })
    @PostMapping("/farm/{farmCode}")
    public ResponseEntity<?> registerInterestFarm(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                           @PathVariable int farmCode) {
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return likeService.registerInterestFarm(userCode, farmCode);
    }


    @Operation(summary = "관심 농장 등록 해제", description = "해당 농장을 관심 농장 등록 해제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 혹은 농장이 존재하지 않음"),
            @ApiResponse(responseCode = "400", description = "관심 농장이 아닌 농장에 대해서 관심 농장 등록 해제를 요청함")
    })
    @DeleteMapping("/farm/{farmCode}")
    public ResponseEntity<?> cancelInterestFarm(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                  @PathVariable int farmCode) {
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return likeService.cancelInterestFarm(userCode, farmCode);
    }

}
