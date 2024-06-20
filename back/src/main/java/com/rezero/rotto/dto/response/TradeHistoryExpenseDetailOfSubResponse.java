package com.rezero.rotto.dto.response;

import com.rezero.rotto.dto.dto.TradeHistoryExpenseDetailOfSubDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TradeHistoryExpenseDetailOfSubResponse {
    private int subscriptionCode;
    private String farmName;
    private int farmCode;
    private int totalSoldPrice;
    private int totalExpense;
    private LocalDateTime tradeTime;
    private int tradeNum;
    private int myProceed;
    private int totalProceed;
    private int refund;
    private int totalTokenCount;
    private List<TradeHistoryExpenseDetailOfSubDto> tradeHistoryExpenseDetailOfSubDtoList;
    private int fee;
}
