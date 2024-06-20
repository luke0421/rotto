package com.rezero.rotto.api.service;

import org.springframework.http.ResponseEntity;

public interface TermService {

    ResponseEntity<?> getTerms(int userCode);
}
