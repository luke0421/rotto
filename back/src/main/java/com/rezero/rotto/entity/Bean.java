package com.rezero.rotto.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bean_tb")
public class Bean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bean_code")
    private int beanCode;

    @Column(name = "bean_name")
    private String beanName;

    @Column(name = "bean_img_path")
    private String beanImgPath;

    @Column(name = "bean_description")
    private String beanDescription;

}
