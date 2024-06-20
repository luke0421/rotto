package com.rezero.rotto.api.service;

import com.rezero.rotto.dto.dto.NewsListDto;
import com.rezero.rotto.dto.response.NewsDetailResponse;
import com.rezero.rotto.dto.response.NewsListResponse;
import com.rezero.rotto.entity.Farm;
import com.rezero.rotto.entity.News;
import com.rezero.rotto.entity.User;
import com.rezero.rotto.repository.NewsRepository;
import com.rezero.rotto.repository.UserRepository;
import com.rezero.rotto.utils.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class NewsServiceImpl implements NewsService {

    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final Pagination pagination;


    // 소식 목록 조회
    public ResponseEntity<?> getNewsList(int userCode, Integer page) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 소식 모두 불러오기
        List<News> allNews = newsRepository.findAll();

        // 인덱스 선언
        int startIdx = 0;
        int endIdx = 0;
        // 총 페이지 수 선언
        int totalPages = 1;

        // 페이지네이션
        List<Integer> indexes = pagination.pagination(page, 10, allNews.size());
        startIdx = indexes.get(0);
        endIdx = indexes.get(1);
        totalPages = indexes.get(2);

        // 최신순으로 보여주기 위해 리스트 뒤집기
        Collections.reverse(allNews);

        // 페이지네이션
        List<News> pageAllNews = allNews.subList(startIdx, endIdx);

        // stream 을 통해 allNews 를 순회하며 dto 리스트에 값을 담는다.
        List<NewsListDto> newsList = pageAllNews.stream()
                .map(news -> new NewsListDto(news.getNewsCode(), news.getTitle(), news.getAuthor(), news.getCategory(),
                        news.getImgLink(), news.getNewsDetailLink(), news.getPostTime()))
                .toList();

        // 리스폰스 생성
        NewsListResponse response = new NewsListResponse(newsList, totalPages);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 소식 상세 조회
    public ResponseEntity<?> getNewsDetail(int userCode, int newsCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 소식 불러오기
        News news = newsRepository.findByNewsCode(newsCode);
        // 소식이 존재하는지 검사
        if (news == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 소식입니다.");
        }
        // 리스폰스 생성
        NewsDetailResponse response = NewsDetailResponse.builder()
                .newsCode(news.getNewsCode())
                .title(news.getTitle())
                .content(news.getContent())
                .author(news.getAuthor())
                .category(news.getCategory())
                .imgLink(news.getImgLink())
                .newsDetailLink(news.getNewsDetailLink())
                .postTime(news.getPostTime())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
