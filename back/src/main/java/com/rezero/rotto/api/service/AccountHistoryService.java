package com.rezero.rotto.api.service;

import org.springframework.http.ResponseEntity;

public interface AccountHistoryService {
    ResponseEntity<?> getAccountHistory(int userCode, int accountCode);

    ResponseEntity<?> getAccountHistoryDeposit(int userCode, int accountCode);

    ResponseEntity<?> getAccountHistoryWithdrawal(int userCode, int accountCode);

 }
