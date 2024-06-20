package com.rezero.rotto.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class FarmDetailResponse {

    private int farmCode;
    private String farmName;
    private String farmCeoName;
    private String farmLogoPath;
    private String farmAddress;
    private int farmScale;
    private String farmIntroduce;
    private LocalDateTime farmStartedDate;
    private String awardHistory;
    private String beanName;
    private Integer beanGrade;
    private BigDecimal returnRate;
    private Boolean isLiked;
    private Boolean isFunding;
    private Long likeCount;
    private Integer deadline;

}
