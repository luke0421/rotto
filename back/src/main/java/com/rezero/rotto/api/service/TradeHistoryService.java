package com.rezero.rotto.api.service;

import org.springframework.http.ResponseEntity;

public interface TradeHistoryService {

    ResponseEntity<?> getTradeHistory(int userCode);
    ResponseEntity<?> getTradeHistoryOwn(int userCode);
    ResponseEntity<?> getExpenseDetailOfSub(int userCode, int subscriptionCode);
    ResponseEntity<?> getHomeInvestInfo(int userCode);
 }
