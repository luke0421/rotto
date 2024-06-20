package com.rezero.rotto.dto.response;

import com.rezero.rotto.dto.dto.NoticeListDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class NoticeListResponse {

    private List<NoticeListDto> notices;

}
