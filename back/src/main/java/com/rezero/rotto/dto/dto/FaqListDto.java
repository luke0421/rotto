package com.rezero.rotto.dto.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class FaqListDto {

    private int faqCode;
    private String title;
    private String content;
}
