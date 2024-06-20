package com.rezero.rotto.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class TermResponse {

    private String title;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
