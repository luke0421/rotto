package com.rezero.rotto.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_tb")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_code")
    private int userCode;

    @Column(name = "name")
    private String name;

    @Column(name = "sex")
    private String sex;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_num", unique = true)
    private String phoneNum;

    @Column(name = "jumin_no")
    private String juminNo;

    @Builder.Default
    @Column(name = "is_delete")
    private Boolean isDelete = false;

    @CreationTimestamp
    @Column(name = "join_time")
    private LocalDateTime joinTime;

    @Column(name = "delete_time")
    private LocalDateTime deleteTime;

    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "email")
    private String email;

    @Builder.Default
    @Column(name = "admin")
    private Boolean admin = false;

    @Column(name = "user_key")
    private String userKey;

    @Column(name = "bc_address")
    private String bcAddress;
}
