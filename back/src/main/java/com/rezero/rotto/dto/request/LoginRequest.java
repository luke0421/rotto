package com.rezero.rotto.dto.request;

import lombok.Getter;

@Getter
public class LoginRequest {

    private String phoneNum;
    private String password;
    private String deviceToken;

}
