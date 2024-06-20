package com.rezero.rotto.dto.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class ReqBoardListDto {

    private int reqBoardCode;
    private String title;
    private LocalDateTime createTime;
}
