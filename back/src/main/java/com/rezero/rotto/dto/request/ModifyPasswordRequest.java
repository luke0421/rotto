package com.rezero.rotto.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ModifyPasswordRequest {

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자 사이여야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).*$",
            message = "비밀번호는 대문자, 소문자, 숫자, 특수문자[!@#$%^&*]를 각각 하나 이상 포함해야 합니다.")
    private String password;

}
