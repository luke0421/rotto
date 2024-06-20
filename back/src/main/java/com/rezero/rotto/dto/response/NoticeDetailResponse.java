package com.rezero.rotto.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class NoticeDetailResponse {

    private String title;
    private String content;
    private LocalDateTime createTime;

}
