package com.rezero.rotto.dto.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class BeanListDto {

    private int beanCode;
    private String beanName;
    private String beanImgPath;

}
