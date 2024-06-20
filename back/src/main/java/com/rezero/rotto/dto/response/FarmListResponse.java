package com.rezero.rotto.dto.response;

import com.rezero.rotto.dto.dto.FarmListDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class FarmListResponse {

    private List<FarmListDto> farms;
    private int totalPages;

}
