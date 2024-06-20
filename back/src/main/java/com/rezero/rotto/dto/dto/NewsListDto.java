package com.rezero.rotto.dto.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class NewsListDto {

    private int newsCode;
    private String title;
    private String author;
    private String category;
    private String imgLink;
    private String newsDetailLink;
    private String postTime;

}
