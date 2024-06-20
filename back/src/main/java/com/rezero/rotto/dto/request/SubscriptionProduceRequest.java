package com.rezero.rotto.dto.request;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionProduceRequest {
    private int farmCode;
    private int confirmPrice;
    private LocalDateTime startedTime;
    private LocalDateTime endedTime;
    private int limitNum;
    private BigDecimal returnRate;
    private int partnerFarmRate;
    private int totalTokenCount;
}
