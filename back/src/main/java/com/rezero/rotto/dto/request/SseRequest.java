package com.rezero.rotto.dto.request;

import com.rezero.rotto.dto.dto.AlertDto;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class SseRequest {

    private int userCode;
    private String name;
    private AlertDto data;

}
