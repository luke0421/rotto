package com.rezero.rotto.repository;

import com.rezero.rotto.entity.ApplyHistory;
import com.rezero.rotto.entity.Farm;
import com.rezero.rotto.entity.Subscription;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class SubscriptionSpecification {

    // 가격 범위에 따른 필터링을 하는 스펙
    public static Specification<Subscription> priceBetween(Integer minPrice, Integer maxPrice){
        return (root, query, criteriaBuilder) -> {

            Predicate priceRangePredicate = criteriaBuilder.conjunction(); // 초기설정
            if (minPrice != null) {
                priceRangePredicate = criteriaBuilder.and(priceRangePredicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("confirmPrice"), minPrice));
            }
            if(maxPrice != null){
                priceRangePredicate = criteriaBuilder.and(priceRangePredicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("confirmPrice"), maxPrice));
            }

            return priceRangePredicate;
        };
    }


    // 청약 상태에 따른 필터링
    public static Specification<Subscription> filterBySubscriptionStatus(Integer subsStatus){
        return (root, query, criteriaBuilder) -> {

            Predicate statusPredicate;
            if (subsStatus == 0) { // 청약예정
                statusPredicate = criteriaBuilder.greaterThan(root.get("startedTime"), criteriaBuilder.currentTimestamp());
            } else if (subsStatus == 1){ // 청약진행중
                statusPredicate = criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(root.get("startedTime"), criteriaBuilder.currentTimestamp()),
                        criteriaBuilder.greaterThanOrEqualTo(root.get("endedTime"), criteriaBuilder.currentTimestamp())
                );
            } else if (subsStatus == 2) { // 청약 종료
                statusPredicate = criteriaBuilder.greaterThan(criteriaBuilder.currentTimestamp(), root.get("endedTime"));
            } else {
                statusPredicate = null;
            }

            return  statusPredicate;
        };
    }

    // 농장 이름에 특정 키워드가 포함되어 있는지 확인하는 스펙
    public static Specification<Subscription> nameContains(String keyword) {
        return (root, query, criteriaBuilder) -> {
            // 서브쿼리 생성
            Subquery<Integer> farmSubquery = query.subquery(Integer.class);
            Root<Farm> farmRoot = farmSubquery.from(Farm.class);

            // 서브쿼리에서 farmCode를 선택
            farmSubquery.select(farmRoot.get("farmCode"))
                    .where(
                            criteriaBuilder.equal(root.get("farmCode"), farmRoot.get("farmCode")),
                            criteriaBuilder.like(farmRoot.get("farmName"), "%" + keyword + "%")
                    );

            // Subscription 엔티티와 Farm 엔티티 사이의 관계를 나타내는 서브쿼리를 쿼리에 적용
            return criteriaBuilder.exists(farmSubquery);
        };
    }

    // 원두 종류에 따른 필터링
    public static Specification<Subscription> filterByBeanType(String beanType) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Integer> farmSubquery = query.subquery(Integer.class);
            Root<Farm> farmRoot = farmSubquery.from(Farm.class);


            // Farm 엔티티와의 관계를 나타내는 서브쿼리를 생성합니다.
            farmSubquery.select(farmRoot.get("farmCode"))
                    .where(criteriaBuilder.equal(root.get("farmCode"), farmRoot.get("farmCode")),
                            criteriaBuilder.equal(farmRoot.get("farmBeanName"), beanType));

            // Subscription 엔티티와 Farm 엔티티 사이의 관계를 나타내는 서브쿼리를 쿼리에 적용합니다.
            return criteriaBuilder.exists(farmSubquery);
        };
    }

    // 요청된 정렬 기준에 따라 정렬하는 스펙, sort = null : 청약 코드 순, rate : 수익률 높은 순, deadline: 마감 기한 빠른순,
    // 조각 가격순(highPrice, lowPrice), highApplyPercent : 신청률 높은순
    public static Specification<Subscription> applySorting(String sort) {
        return (root, query, criteriaBuilder) -> {
            if ("rate".equals(sort)) {
                // 수익률 높은 순으로 정렬
                query.orderBy(criteriaBuilder.desc(root.get("returnRate")));
            } else if ("deadline".equals(sort) || "highApplyPercent".equals(sort)) {
                query.where(criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(root.get("startedTime"), criteriaBuilder.currentTimestamp()),
                        criteriaBuilder.greaterThanOrEqualTo(root.get("endedTime"), criteriaBuilder.currentTimestamp())
                ));
                // 청약 마감 기한 빠른 순
                if ("deadline".equals(sort)) {
                    query.orderBy(criteriaBuilder.asc(root.get("endedTime")));
                // sort = highApplyPercent. 신청률 높은 순
                } else if ("highApplyPercent".equals(sort)) {
                    Subquery<Long> applyHistoryCountSubquery = query.subquery(Long.class);
                    Root<ApplyHistory> applyHistoryRoot = applyHistoryCountSubquery.from(ApplyHistory.class);
                    applyHistoryCountSubquery.select(criteriaBuilder.count(applyHistoryRoot));

                    // Subscription 엔티티와 ApplyHistory 엔티티의 관계를 나타내는 서브쿼리를 생성합니다.
                    applyHistoryCountSubquery.where(criteriaBuilder.equal(root.get("subscriptionCode"), applyHistoryRoot.get("subscriptionCode")));

                    // 총 신청내역 갯수를 반환합니다.
                    Subquery<Double> totalAmountSubquery = query.subquery(Double.class);
                    Root<ApplyHistory> totalAmountRoot = totalAmountSubquery.from(ApplyHistory.class);
                    totalAmountSubquery.select(criteriaBuilder.sum(totalAmountRoot.get("applyCount")));

                    // Subscription 엔티티와 ApplyHistory 엔티티의 관계를 나타내는 서브쿼리를 생성합니다.
                    totalAmountSubquery.where(criteriaBuilder.equal(root.get("subscriptionCode"), totalAmountRoot.get("subscriptionCode")));

                    // 총 발행토큰 수를 반환합니다.
                    Expression<Double> totalAmount = totalAmountSubquery.getSelection();
                    Expression<Double> totalTokenCount = root.get("totalTokenCount");

                    // 퍼센테이지를 계산합니다.
                    Expression<Number> percentage = criteriaBuilder.quot(totalAmount, totalTokenCount);

                    // 최종 결과를 퍼센테이지 순으로 정렬합니다.
                    query.orderBy(criteriaBuilder.desc(percentage));
                }
            // 가격 높은순으로 정렬
            } else if ("highPrice".equals(sort)) {
                query.orderBy(criteriaBuilder.desc(root.get("confirmPrice")));
            // 가격 낮은순으로 정렬
            } else if ("lowPrice".equals(sort)) {
                query.orderBy(criteriaBuilder.asc(root.get("confirmPrice")));
            // 기본 정렬
            } else {
                query.orderBy(criteriaBuilder.desc(root.get("subscriptionCode")));
            }

            return query.getRestriction();
        };
    }
}
