package com.rezero.rotto.dto.response;

import com.rezero.rotto.dto.dto.AlertListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AlertListResponse {

    private List<AlertListDto> alerts;

}
