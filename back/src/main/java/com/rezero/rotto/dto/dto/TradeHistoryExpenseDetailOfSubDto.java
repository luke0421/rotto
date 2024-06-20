package com.rezero.rotto.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TradeHistoryExpenseDetailOfSubDto {

    private String expenditureContent;
    private int expenses;
}
