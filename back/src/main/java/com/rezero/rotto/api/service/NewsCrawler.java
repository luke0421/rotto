package com.rezero.rotto.api.service;

import com.rezero.rotto.entity.News;
import com.rezero.rotto.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsCrawler {
    
    private final NewsRepository newsRepository;

    @Scheduled(fixedRate = 3600000) // 1시간마다 크롤링 수행
    void crawlCoffeeNews() {
        log.info("Crawling Start");

        // 크롤링 개수
        int crawlCount = 0;

        try {
            // 커피 뉴스 크롤링할 URL
            String url = "https://dailycoffeenews.com/latest-news/";

            // 연결하기
            Document document = Jsoup.connect(url).get();

            // article 요소 선택
            Elements articles = document.select("article");


            // 각 article 요소에 대해 반복
            for (Element article : articles) {
                // News 엔티티 생성
                News news = new News();

                String datePart = null;
                String headlineText = null;

                // 기사 detail 링크 가져오기
                Element link = article.selectFirst("h2 > a");
                if (link != null) {
                    // link 에서 href 부분 가져오기
                    String href = link.attr("href");
                    // 이미 db에 존재하면 크롤링하지 않음
                    if (newsRepository.existsByNewsDetailLink(href)) {
                        continue;
                    }
                    news.setNewsDetailLink(href);
                } else {
                    continue;
                }

                // 카테고리 가져오기
                Element parentCategory = article.selectFirst(".parent-category");
                if (parentCategory != null) {
                    String categoryText = parentCategory.text();
                    String[] categoryParts = categoryText.split(">");
                    String categoryPart = categoryParts[1].trim();
                    news.setCategory(categoryPart);
                } else {
                    continue;
                }

                // 게시일 가져오기
                Element dateElement = article.selectFirst("p.byline-date");
                if (dateElement != null) {
                    // 게시일은 "| May 29, 2024"와 같은 형태로 가져와지므로 파싱한다.
                    String dateText = dateElement.ownText().trim();
                    String[] dateParts = dateText.split("\\|");
                    datePart = dateParts[2].trim();
                    news.setPostTime(datePart);
                } else {
                    continue;
                }

                // 헤드라인 가져오기
                Element headlineLink = article.selectFirst("h2 > a");
                if (headlineLink != null) {
                    headlineText = headlineLink.text();
                    news.setTitle(headlineText);
                } else {
                    continue;
                }

                // 이미지의 스타일 속성 값 가져오기
                Element imageDiv = article.selectFirst(".blog-featured-bg-image");
                if (imageDiv != null) {
                    String style = imageDiv.attr("style");
                    // 스타일 속성 값에서 이미지 URL을 추출합니다.
                    String imageUrl = extractImageUrlFromStyle(style);
                    news.setImgLink(imageUrl);
                }

                // 작성자 가져오기
                Element authorLink = article.selectFirst("p.byline-date > a");
                if (authorLink != null) {
                    String authorText = authorLink.text();
                    news.setAuthor(authorText);
                }

                try {
                    // 뉴스 디테일도 추가
                    // 커피 뉴스 디테일 URL
                    String parseDate = parseDateToUrl(datePart);
                    String parseHeadline = removeSpecialCharacters(headlineText);
                    String detailUrl = url + parseDate + "/" + parseHeadline;

                    if (parseDate == null) {
                        continue;
                    }
                    Document detailDocument = Jsoup.connect(detailUrl).get();

                    // entry 안에 있는 모든 p 태그들을 하나의 문자열로 합칠 변수
                    StringBuilder entryContent = new StringBuilder();

                    // 크롤링할 요소 선택
                    Elements entry = detailDocument.select(".entry");

                    // entry 에 있는 각 p 태그에 대해 반복
                    for (Element paragraph : entry.select("p")) {
                        // p 태그의 텍스트를 가져와서 entryContent에 추가
                        entryContent.append(paragraph.text()).append("\n");
                    }

                    // News - content 에 넣기
                    String combinedText = entryContent.toString();
                    news.setContent(combinedText);

                    // 데이터 저장
                    newsRepository.save(news);
                    crawlCount ++;
                } catch (IOException e) {
                    log.error("뉴스 디테일 크롤링 실패" + news.getNewsDetailLink(), e);
                }
            }
        } catch (IOException e) {
            log.error("뉴스 크롤링 실패", e);
        }
        log.info(crawlCount + " Data Crawling Success");
    }

    // 스타일 속성 값에서 이미지 URL을 추출하는 메서드
    private static String extractImageUrlFromStyle(String style) {
        String imageUrl = "";
        String[] parts = style.split("\\(");
        if (parts.length > 1) {
            String urlPart = parts[1].split("\\)")[0];
            imageUrl = urlPart.replaceAll("'", "").replaceAll("\"", "");
        }
        return imageUrl;
    }


    // "May 14, 2024" 와 같은 데이터를 2024/5/14 로 파싱하는 메서드
    public static String parseDateToUrl(String dateString) {
        try {
            // 날짜 문자열을 LocalDate 객체로 파싱
            DateTimeFormatter inputFormatter1 = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
            LocalDate date1 = null;
            try {
                date1 = LocalDate.parse(dateString, inputFormatter1);
            } catch (DateTimeParseException ignored) {}

            DateTimeFormatter inputFormatter2 = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
            LocalDate date2 = null;
            try {
                date2 = LocalDate.parse(dateString, inputFormatter2);
            } catch (DateTimeParseException ignored) {}

            // 두 날짜 중 하나를 선택
            LocalDate date = (date1 != null) ? date1 : date2;

            if (date == null) {
                throw new DateTimeParseException("Failed to parse date", dateString, 0);
            }

            // 원하는 형식으로 변환
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            return date.format(outputFormatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null; // 파싱 실패 시 null 반환
        }
    }

    public static String removeSpecialCharacters(String input) {
        // 특수 문자를 제거하고 공백을 '-'로 변경하는 정규 표현식
        String result = input.replaceAll("[^a-zA-Z0-9\\s-]", "").replaceAll("\\s", "-");
        return result;
    }
}
