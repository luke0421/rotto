package com.rezero.rotto.dto.response;

import com.rezero.rotto.dto.dto.FarmListDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class FarmTop10ListResponse {

    private List<FarmListDto> farms;

}
