package com.rezero.rotto.dto.response;

import com.rezero.rotto.dto.dto.TradeHistoryOwnListDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TradeHistoryOwnListResponse {
    private List<TradeHistoryOwnListDto> tradeHistoryOwnListDtoss;
}
