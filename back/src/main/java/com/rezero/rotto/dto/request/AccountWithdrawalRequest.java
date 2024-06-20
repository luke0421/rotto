package com.rezero.rotto.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountWithdrawalRequest {
    private String transactionBalance;
}
