package com.rezero.rotto.api.service;

import com.rezero.rotto.dto.request.SseRequest;
import org.springframework.http.ResponseEntity;

public interface AlertService {

    ResponseEntity<?> getAlertList(int userCode);
    ResponseEntity<?> getAlertDetail(int userCode, int alertCode);
    ResponseEntity<?> deleteAlert(int userCode, int alertCode);
    ResponseEntity<?> readAllAlert(int userCode);
    ResponseEntity<?> deleteAllAlert(int userCode);
    boolean createAlert(SseRequest request);

}
