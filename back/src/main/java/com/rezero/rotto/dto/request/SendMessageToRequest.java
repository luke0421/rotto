package com.rezero.rotto.dto.request;

import lombok.Getter;

@Getter
public class SendMessageToRequest {

    private int userCode;
    private String title;
    private String body;

}
