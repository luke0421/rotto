package com.rezero.rotto.dto.request;

import com.rezero.rotto.entity.Subscription;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistributeRequest {
	private Subscription subscription;
	private int userCode;
	private int amount;
}
