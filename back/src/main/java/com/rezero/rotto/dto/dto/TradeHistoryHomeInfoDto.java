package com.rezero.rotto.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TradeHistoryHomeInfoDto {
    private String title;
    private int tokenNum;
    private int expenses;
}
