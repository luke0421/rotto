package com.rezero.rotto.api.service;

import org.springframework.http.ResponseEntity;

public interface LikeService {

    ResponseEntity<?> registerInterestFarm(int userCode, int farmCode);
    ResponseEntity<?> cancelInterestFarm(int userCode, int farmCode);

}
