package com.rezero.rotto.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CreateFinanceAccountRequest {
    private String apiKey;
    private String userId;
}
