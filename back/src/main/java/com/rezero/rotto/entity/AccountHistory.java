package com.rezero.rotto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account_history_tb")
public class AccountHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_history_code")
    private int applyHistoryCode;

    @Column(name = "account_code")
    private int accountCode;

    @Column(name = "amount")
    private int amount;

    @Column(name = "account_time")
    private LocalDateTime accountTime;

    @Column(name = "deposit_or_withdrawal", columnDefinition = "TINYINT(1)")
    private int depositWithdrawalCode;

}
