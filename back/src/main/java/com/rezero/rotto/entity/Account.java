package com.rezero.rotto.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account_tb")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_code")
    private int accountCode;

    @Column(name = "user_code")
    private int userCode;

    @Column(name = "balance")
    private int balance;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_num")
    private String accountNum;

    @Column(name = "account_type", columnDefinition = "TINYINT(1) DEFAULT 0")
    private int accountType;
}
