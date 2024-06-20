package com.rezero.rotto.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CheckEmailRequest {

    private String email;

}
