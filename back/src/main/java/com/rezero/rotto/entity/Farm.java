package com.rezero.rotto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "farm_tb")
public class Farm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "farm_code")
    private int farmCode;

    @Column(name = "farm_name")
    private String farmName;

    @Column(name = "farm_ceo_name")
    private String farmCeoName;

    @Column(name = "farm_logo_path")
    private String farmLogoPath;

    @Column(name = "farm_address")
    private String farmAddress;

    @Column(name = "farm_scale")
    private int farmScale;

    @Column(name = "farm_introduce")
    private String farmIntroduce;

    @Column(name = "farm_started_time")
    private LocalDateTime farmStartedTime;

    @Column(name = "award_history")
    private String awardHistory;

    @Column(name = "farm_bean_name")
    private String farmBeanName;

    @Column(name = "farm_bean_grade")
    private int farmBeanGrade;

}
