package com.rezero.rotto.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class ReqBoardDetailRegisterModifyResponse {
    private int reqBoardCode;
    private String title;
    private String contents;
    private LocalDateTime createTime;
}
