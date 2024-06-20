package com.rezero.rotto.dto.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AlertListDto {

    private int alertCode;
    private String title;
    private String alertType;
    private LocalDateTime createTime;
    private Boolean isRead;

}
