package com.rezero.rotto.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alert_tb")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_code")
    private int alertCode;

    @Column(name = "user_code")
    private int userCode;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "alert_type")
    private String alertType;

    @Builder.Default
    @Column(name = "is_read")
    private Boolean isRead = false;

    @CreationTimestamp
    @Column(name = "create_time")
    private LocalDateTime createTime;

}
