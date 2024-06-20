package com.rezero.rotto.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "news_tb")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_code")
    private int newsCode;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "author")
    private String author;

    @Column(name = "category")
    private String category;

    @Column(name = "img_link")
    private String imgLink;

    @Column(name = "news_detail_link")
    private String newsDetailLink;

    @Column(name = "post_time")
    private String postTime;

    @CreationTimestamp
    @Column(name = "create_time")
    private LocalDateTime createTime;

}
