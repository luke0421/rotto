package com.rezero.rotto.api.service;

import org.springframework.http.ResponseEntity;

public interface ApplyHistoryService {

    // 청약신청
    ResponseEntity<?> postApply(int userCode, int subscriptionCode, int applyCount);

    // 청약 신청 취소
    ResponseEntity<?> deleteApply(int userCode, int subscriptionCode);

    ResponseEntity<?> getApply(int userCode);

    ResponseEntity<?> getApplyTerminated(int userCode);
}
