package com.rezero.rotto.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class SubscriptionDetailResponse {
    private int subscriptionCode;
    private int farmCode;
    private String farmName;
    private int confirmPrice;
    private LocalDateTime startedTime;
    private LocalDateTime endTime;
    private int limitNum;
    private BigDecimal returnRate;
    private int applyCount;
    private int totalTokenCount;
    private int subsStatus;
    private String farmImg;
    private int isApply;
}
