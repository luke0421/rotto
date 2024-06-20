package com.rezero.rotto.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "interest_farm_tb")
public class InterestFarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_farm_code")
    private int interestFarmCode;

    @Column(name = "user_code")
    private int userCode;

    @Column(name = "farm_code")
    private int farmCode;

}
