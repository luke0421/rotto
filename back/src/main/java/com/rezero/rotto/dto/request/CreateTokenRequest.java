package com.rezero.rotto.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTokenRequest {
	private Integer code;
	private Integer amount;
}
