package com.rezero.rotto.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class AccountOneResponse {
    private int accountCode;
    private String bankName;
    private String accountHolder;
    private String accountNum;
    private int accountBalance;
    private int accountType;
}

