package com.rezero.rotto.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class FaqDetailResponse {

    private int faqCode;
    private String title;
    private String contents;
}
