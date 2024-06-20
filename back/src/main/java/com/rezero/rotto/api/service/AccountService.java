package com.rezero.rotto.api.service;

import com.rezero.rotto.dto.request.AccountConnectionRequest;
import com.rezero.rotto.dto.request.AccountDepositRequest;
import com.rezero.rotto.dto.request.AccountWithdrawalRequest;
import org.springframework.http.ResponseEntity;

public interface AccountService {

    // 공모계좌 조회
    ResponseEntity<?> getAccountZero(int userCode);

    // 연결진짜계좌 조회
    ResponseEntity<?> getAccountOne(int userCode);

    // 진짜계좌연결
    ResponseEntity<?> postAccountConnection(int userCode, AccountConnectionRequest accountConnectionRequest);

    // 공모계좌 출금
    ResponseEntity<?> patchAccountWithdrawal(int userCode, AccountWithdrawalRequest accountWithdrawalRequest);

    // 공모계좌 입금
    ResponseEntity<?> patchAccountDeposit(int userCode, AccountDepositRequest accountDepositRequest);

    // 연결계좌 연결해제
    ResponseEntity<?> deleteAccountConnection(int userCode, int accountCode);
}
