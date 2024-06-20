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
public class NewsDetailResponse {

    private int newsCode;
    private String title;
    private String content;
    private String author;
    private String category;
    private String imgLink;
    private String newsDetailLink;
    private String postTime;

}
