package com.rezero.rotto.api.service;

//import com.rezero.rotto.dto.request.RegisterPinRequest;

import com.rezero.rotto.dto.request.CheckEmailRequest;
import com.rezero.rotto.dto.request.CheckPhoneNumRequest;
import com.rezero.rotto.dto.request.ModifyPasswordRequest;
import com.rezero.rotto.dto.request.SignUpRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> signUp(SignUpRequest request);

    ResponseEntity<?> getUserInfo(int userCode);

    ResponseEntity<String> deleteUser(int userCode);

    ResponseEntity<?> checkPhoneNum(CheckPhoneNumRequest request);

    ResponseEntity<?> checkEmail(CheckEmailRequest request);

    ResponseEntity<?> modifyPassword(int userCode, ModifyPasswordRequest request);

    ResponseEntity<?> updateBCAddress(int userCode, String address);
}
