package com.rezero.rotto.dto.response;

import com.rezero.rotto.dto.dto.ApplyHistoryListCancelDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ApplyHistoryListCancelResponse {
    private List<ApplyHistoryListCancelDto> userApplyHistoryListCancelDtos;
}
