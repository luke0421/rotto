package com.rezero.rotto.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AlertDetailResponse {

    private String title;
    private String content;
    private String alertType;
    private LocalDateTime createTime;

}
