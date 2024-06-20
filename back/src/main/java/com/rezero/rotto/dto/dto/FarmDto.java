package com.rezero.rotto.dto.dto;

import lombok.*;

import java.math.BigDecimal;

@RequiredArgsConstructor
public abstract class FarmDto {

    private int farmCode;
    private String farmName;
    private String farmLogoPath;
    private String beanName;
    private boolean isLiked;
    private BigDecimal returnRate;
    private Boolean isFunding;
    private Long likeCount;

}
