package com.rezero.rotto.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class AccountConnectionResponse {
    private int accountCode;
    private String accountNum;
    private String bankName;
    private int accountType;
}
