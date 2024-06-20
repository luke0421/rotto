package com.rezero.rotto.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "farm_top10_tb")
public class FarmTop10 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "farm_top10_code")
    private int farmTop10Code;

    @Column(name = "farm_code")
    private int farmCode;

}
