package com.rezero.rotto.dto.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Builder
@Getter
@Setter
public class ApplyHistoryListGetDto {

    private int applyHistoryCode;
    private int subscriptionCode;
    private int userCode;
    private int farmCode;
    private String farmName;
    private int confirmPrice;
    private LocalDateTime applyTime;
    private LocalDateTime startedTime;
    private LocalDateTime endedTime;
    private int applyCount;
    private int totalTokenCount;
    private int totalApplyCount;
    private LocalDateTime refundDate;
}
