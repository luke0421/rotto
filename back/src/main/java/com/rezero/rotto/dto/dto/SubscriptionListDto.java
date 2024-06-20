package com.rezero.rotto.dto.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class SubscriptionListDto {

    private int subscriptionCode;
    private int farmCode;
    private String farmName;
    private int confirmPrice;
    private LocalDateTime startedTime;
    private LocalDateTime endTime;
    private String beanType;
    private int limitNum;
    private BigDecimal returnRate;
    private Integer applyCount;
    private int totalTokenCount;
    private int subsStatus;

}
