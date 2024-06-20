package com.rezero.rotto.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountConnectionRequest {
    private String accountNum;
    private String bankName;
}
