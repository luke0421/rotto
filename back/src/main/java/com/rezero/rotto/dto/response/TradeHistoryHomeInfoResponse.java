package com.rezero.rotto.dto.response;

import com.rezero.rotto.dto.dto.TradeHistoryHomeInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Builder
@AllArgsConstructor
public class TradeHistoryHomeInfoResponse {
    private int totalInvestAmount;
    private int totalProceed;
    private double totalPercent;
    List<TradeHistoryHomeInfoDto> tradeHistoryHomeInfoDtoss;
}
