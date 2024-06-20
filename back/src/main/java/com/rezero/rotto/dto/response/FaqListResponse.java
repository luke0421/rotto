package com.rezero.rotto.dto.response;

import com.rezero.rotto.dto.dto.FaqListDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class FaqListResponse {

    private List<FaqListDto> faqListDtos;
}
