package com.rezero.rotto.dto.response;

import com.rezero.rotto.dto.dto.NewsListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class NewsListResponse {

    private List<NewsListDto> newsList;
    private int totalPages;

}
