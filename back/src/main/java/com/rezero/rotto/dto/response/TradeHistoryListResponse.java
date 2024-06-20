package com.rezero.rotto.dto.response;

import com.rezero.rotto.dto.dto.TradeHistoryListDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TradeHistoryListResponse {

    private List<TradeHistoryListDto> tradeHistoryListDtoss;
}
