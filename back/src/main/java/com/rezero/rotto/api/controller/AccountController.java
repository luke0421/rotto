package com.rezero.rotto.api.controller;

import com.rezero.rotto.api.service.AccountService;
import com.rezero.rotto.dto.request.AccountConnectionRequest;
import com.rezero.rotto.dto.request.AccountDepositRequest;
import com.rezero.rotto.dto.request.AccountWithdrawalRequest;
import com.rezero.rotto.dto.response.AccountConnectionResponse;
import com.rezero.rotto.dto.response.AccountOneResponse;
import com.rezero.rotto.dto.response.AccountZeroResponse;
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
@RequestMapping("/account")
@RequiredArgsConstructor
@Tag(name = "Account 컨트롤러", description = "계좌관련 Api")
public class AccountController {

    private final AccountService accountService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "rotto 계좌 조회",
            description = "내 로또계좌 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200", description = "rotto 계좌조회 성공",
                    content = @Content(schema = @Schema(implementation = AccountZeroResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 요청")
    })

    @GetMapping
    public ResponseEntity<?> getAccountZero(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return accountService.getAccountZero(userCode);
    }


    @Operation(summary = "진짜 계좌 조회",
            description = "내 진짜 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200", description = "진짜 계좌조회 성공",
                    content = @Content(schema = @Schema(implementation = AccountOneResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 요청"),
            @ApiResponse(responseCode = "500", description = "연결계좌가 없습니다.")

    })

    @GetMapping("/real")
    public ResponseEntity<?> getAccountOne(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return accountService.getAccountOne(userCode);
    }


    @Operation(summary = "진짜 계좌 연결",
            description = "진짜 내 계좌 연결")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200", description = "진짜 내 계좌 연결성공",
                    content = @Content(schema = @Schema(implementation = AccountConnectionResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 요청")
    })

    @PostMapping("/connection")
    public ResponseEntity<?> postAccountConnection(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody  AccountConnectionRequest accountConnectionRequest){
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return accountService.postAccountConnection(userCode, accountConnectionRequest);
    }


    @Operation(summary = "rotto(공모)계좌 출금(보내기)",
            description = "공모계좌 출금 (공모계좌 -> 진짜계좌)")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200", description = "공모계좌 출금 성공"),
//                    content = @Content(schema = @Schema(implementation = AccountConnectionResponse.class)))
            @ApiResponse(responseCode = "404", description = "존재하지 않는 요청")
    })

    @PatchMapping("/withdrawal")
    public ResponseEntity<?> patchAccountWithdrawal(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody AccountWithdrawalRequest accountWithdrawalRequest){
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return accountService.patchAccountWithdrawal(userCode, accountWithdrawalRequest);
    }


    @Operation(summary = "rotto(공모)계좌 입금(채우기)",
            description = "공모계좌 입금(진짜계좌 -> 공모계좌)")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200", description = "공모계좌 입금 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 요청")
    })

    @PatchMapping("/deposit")
    public ResponseEntity<?> patchAccountDeposit(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody AccountDepositRequest accountDepositRequest){
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return accountService.patchAccountDeposit(userCode, accountDepositRequest);
    }


    @Operation(summary = "진짜계좌 삭제",
            description = "진짜계좌 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200", description = "연결계좌 삭제 성공"),
//                    content = @Content(schema = @Schema(implementation = AccountConnectionResponse.class)))
            @ApiResponse(responseCode = "404", description = "존재하지 않는 요청")
    })

    @DeleteMapping("/real/remove/{accountCode}")
    public ResponseEntity<?> deleteAccountConnection(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable int accountCode){
        int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
        return accountService.deleteAccountConnection(userCode, accountCode);
    }

}
