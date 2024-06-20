package com.rezero.rotto.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CheckPhoneNumRequest {

    @NotBlank(message = "입력된 값이 없습니다.")
    @Size(min = 11, max = 11, message = "휴대폰 번호는 11자리여야 합니다.")
    @Pattern(regexp = "^010\\d{8}", message = "휴대폰 번호는 010으로 시작해야하며, 숫자만을 포함하여야 합니다.")
    private String phoneNum;

}
