//package com.rezero.rotto;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.junit.jupiter.api.Test;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class CrawlingTest {
//
//    @Test
//    void crawlCoffeeNews() {
//        try {
//            String url = "https://dailycoffeenews.com/latest-news";
//
//            Document document = Jsoup.connect(url).get();
//
//            // 해당 페이지의 모든 article 요소를 선택합니다.
//            Elements articles = document.select("article");
//
//            // 각 article 요소에 대해 반복합니다.
//            for (Element article : articles) {
//
//                // a 태그의 href 속성을 가져옵니다.
//                Element link = article.selectFirst("h2 > a");
//                if (link != null) {
//                    String href = link.attr("href");
//                    System.out.println("Link: " + href);
//                } else {
//                    continue;
//                }
//
//                // parent-category 클래스 안의 텍스트를 가져옵니다.
//                Element parentCategory = article.selectFirst(".parent-category");
//                if (parentCategory != null) {
//                    String categoryText = parentCategory.text();
//                    String[] categoryParts = categoryText.split(">");
//                    String categoryPart = categoryParts[1].trim();
//                    System.out.println("Category: " + categoryPart);
//                } else {
//                    continue;
//                }
//
//                // byline-date 클래스 안의 텍스트를 가져옵니다.
//                Element dateElement = article.selectFirst("p.byline-date");
//                if (dateElement != null) {
//                    String dateText = dateElement.ownText().trim();
//                    String[] dateParts = dateText.split("\\|");
//
//                    String datePart = dateParts[2].trim();
//                    System.out.println("Date: " + datePart);
//                } else {
//                    continue;
//                }
//
//                // h2 태그 안의 첫 번째 a 태그를 선택합니다.
//                Element headlineLink = article.selectFirst("h2 > a");
//
//                // a 태그가 존재하는 경우 텍스트를 가져옵니다.
//                if (headlineLink != null) {
//                    String headlineText = headlineLink.text();
//                    System.out.println("Headline: " + headlineText);
//                } else {
//                    continue;
//                }
//
//                // 이미지의 스타일 속성 값을 가져옵니다.
//                Element imageDiv = article.selectFirst(".blog-featured-bg-image");
//                if (imageDiv != null) {
//                    String style = imageDiv.attr("style");
//                    // 스타일 속성 값에서 이미지 URL을 추출합니다.
//                    String imageUrl = extractImageUrlFromStyle(style);
//                    System.out.println("Image URL: " + imageUrl);
//                }
//
//                // author 클래스 안의 텍스트를 가져옵니다.
//                Element authorLink = article.selectFirst("p.byline-date > a");
//                if (authorLink != null) {
//                    String authorText = authorLink.text();
//                    System.out.println("Author: " + authorText);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    // 스타일 속성 값에서 이미지 URL을 추출하는 메서드
//    private static String extractImageUrlFromStyle(String style) {
//        String imageUrl = "";
//        String[] parts = style.split("\\(");
//        if (parts.length > 1) {
//            String urlPart = parts[1].split("\\)")[0];
//            imageUrl = urlPart.replaceAll("'", "").replaceAll("\"", "");
//        }
//        return imageUrl;
//    }
//
//}
