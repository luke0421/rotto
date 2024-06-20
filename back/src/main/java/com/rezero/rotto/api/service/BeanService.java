package com.rezero.rotto.api.service;

import org.springframework.http.ResponseEntity;

public interface BeanService {

    ResponseEntity<?> getBeanList(int userCode);
    ResponseEntity<?> getBeanDetail(int userCode, int beanCode);

}
