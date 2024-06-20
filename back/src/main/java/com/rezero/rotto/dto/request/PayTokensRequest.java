package com.rezero.rotto.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayTokensRequest {
	private int code;
	private String address;
	private int amount;
}
