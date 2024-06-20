package com.rezero.rotto.api.service;

import org.springframework.http.ResponseEntity;

public interface FaqService {

    // faq list 조회
    ResponseEntity<?> getFaqList(int userCode);

}
