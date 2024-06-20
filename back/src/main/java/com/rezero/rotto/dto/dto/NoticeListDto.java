package com.rezero.rotto.dto.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class NoticeListDto {

    private int noticeCode;
    private String title;
    private LocalDateTime createTime;

}
