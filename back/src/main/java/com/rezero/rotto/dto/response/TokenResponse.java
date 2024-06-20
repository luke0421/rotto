package com.rezero.rotto.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TokenResponse {

    private String grantType; // Bearer
    private String accessToken;
    private String refreshToken;

}
