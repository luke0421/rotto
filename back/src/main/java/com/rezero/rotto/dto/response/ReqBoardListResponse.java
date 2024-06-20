package com.rezero.rotto.dto.response;

import com.rezero.rotto.dto.dto.ReqBoardListDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ReqBoardListResponse {

    private List<ReqBoardListDto> reqBoardListDtos;

}
