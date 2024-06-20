package com.rezero.rotto.dto.response;

import com.rezero.rotto.dto.dto.BeanListDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class BeanListResponse {

    private List<BeanListDto> beans;

}
