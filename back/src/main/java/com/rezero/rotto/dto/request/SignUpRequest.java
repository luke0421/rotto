package com.rezero.rotto.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignUpRequest {

    @NotBlank(message = "이름은 필수입니다.")
    @Size(min = 2, max = 20, message = "이름은 2자 이상, 20자 이하여야 합니다.")
    private String name;
    @NotBlank(message = "성별은 필수입니다.")
    @Pattern(regexp = "^[MF]$", message = "성별은 M 또는 F 이어야 합니다.")
    private String sex;
    @NotBlank(message = "휴대폰 번호는 필수입니다.")
    @Size(min = 11, max = 11, message = "휴대폰 번호는 11자리여야 합니다.")
    @Pattern(regexp = "^010\\d{8}", message = "휴대폰 번호는 010으로 시작해야하며, 숫자만을 포함하여야 합니다.")
    private String phoneNum;
    @NotBlank(message = "주민등록번호는 필수입니다.")
    @Size(min = 6, max = 6, message = "주민등록번호는 6자리여야 합니다.")
    @Pattern(regexp = "\\d{6}", message = "주민등록번호는 숫자만을 포함하여야 합니다.")
    private String juminNo;
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자 사이여야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).*$",
            message = "비밀번호는 대문자, 소문자, 숫자, 특수문자[!@#$%^&*]를 각각 하나 이상 포함해야 합니다.")
    private String password;

    private String email;

}
