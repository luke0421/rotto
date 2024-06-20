package com.rezero.rotto.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@Table(name = "trade_history_tb")
@NoArgsConstructor
@AllArgsConstructor
public class TradeHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "trade_history_code")
	private int tradeHistoryCode;

	@Column(name = "user_code")
	private int userCode;

	@Column(name = "subscription_code")
	private int subscriptionCode;

	@CreationTimestamp
	@Column(name = "trade_time")
	private LocalDateTime tradeTime;

	@Column(name = "trade_num")
	private int tradeNum;

	@Column(name = "refund")
	private int refund;

	@Column(name = "bc_address")
	private String bcAddress;

	@Column(name = "token_price")
	private double tokenPrice;
}
