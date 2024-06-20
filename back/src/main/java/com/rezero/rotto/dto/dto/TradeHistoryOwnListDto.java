package com.rezero.rotto.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TradeHistoryOwnListDto {
    private int subscriptionCode;
    private int farmCode;
    private String farmName;
    private int confirmPrice;
    private LocalDateTime tradeTime;
    private int tradeNum;
    private int refund;
    private int proceed;
    private int totalTokenCount;
}
