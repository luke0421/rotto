package com.rezero.rotto.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AccountZeroResponse {

    private int accountCode;
    private String bankName;
    private String accountHolder;
    private String accountNum;
    private int accountBalance;
    private int accountType;
}
