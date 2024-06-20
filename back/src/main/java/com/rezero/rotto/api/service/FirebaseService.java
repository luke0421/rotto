package com.rezero.rotto.api.service;

import com.rezero.rotto.dto.request.SendMessageToAllUsersRequest;
import com.rezero.rotto.dto.request.SendMessageToRequest;
import org.springframework.http.ResponseEntity;

public interface FirebaseService {

    ResponseEntity<?> sendMessageTo(SendMessageToRequest request);

    ResponseEntity<?> sendMessageToAllUsers(SendMessageToAllUsersRequest request);

    boolean sendMessage(String token, String title, String body);

}
