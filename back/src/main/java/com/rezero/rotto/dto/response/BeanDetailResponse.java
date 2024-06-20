package com.rezero.rotto.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BeanDetailResponse {

    private int beanCode;
    private String beanName;
    private String beanDescription;
    private String beanImgPath;

}
