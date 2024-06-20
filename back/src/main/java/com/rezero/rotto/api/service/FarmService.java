package com.rezero.rotto.api.service;

import org.springframework.http.ResponseEntity;

public interface FarmService {

    ResponseEntity<?> getFarmList(int userCode, Integer page, Boolean isLiked, Integer subsStatus, Integer minPrice, Integer maxPrice, String beanType, String sort, String keyword);
    ResponseEntity<?> getFarmDetail(int userCode, int farmCode);
    ResponseEntity<?> getRateTop10FarmList(int userCode);

}
