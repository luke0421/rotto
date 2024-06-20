package com.rezero.rotto.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class ApplyHistoryResponse {

    private int applyHistoryCode;
    private int userCode;
    private int subscriptionCode;
    private int confirmPrice;
    private LocalDateTime applyTime;
    private int isDelete;
    private int applyCount;

}
