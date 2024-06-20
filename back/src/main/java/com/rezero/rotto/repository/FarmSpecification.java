package com.rezero.rotto.repository;

import com.rezero.rotto.entity.Farm;
import com.rezero.rotto.entity.InterestFarm;
import com.rezero.rotto.entity.Subscription;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

public class FarmSpecification {


    // 가격 범위에 따른 필터링을 하는 스펙
    public static Specification<Farm> priceBetween(Integer minPrice, Integer maxPrice) {
        return (root, query, criteriaBuilder) -> {
            // 가격 조건에 맞는 Subscription을 찾는 서브쿼리
            Subquery<Integer> priceSubquery = query.subquery(Integer.class);
            Root<Subscription> subscriptionRoot = priceSubquery.from(Subscription.class);
            priceSubquery.select(subscriptionRoot.get("farmCode"));

            // 가격 범위 설정
            Predicate priceRangePredicate = criteriaBuilder.conjunction(); // 초기 조건은 항상 참
            if (minPrice != null) {
                priceRangePredicate = criteriaBuilder.and(priceRangePredicate,
                        criteriaBuilder.greaterThanOrEqualTo(subscriptionRoot.get("confirmPrice"), minPrice));
            }
            if (maxPrice != null) {
                priceRangePredicate = criteriaBuilder.and(priceRangePredicate,
                        criteriaBuilder.lessThanOrEqualTo(subscriptionRoot.get("confirmPrice"), maxPrice));
            }

            priceSubquery.where(
                    criteriaBuilder.and(
                            priceRangePredicate,
                            criteriaBuilder.equal(subscriptionRoot.get("farmCode"), root.get("farmCode"))
                    )
            );

            // 최종적으로 Farm 테이블에서 farmCode가 서브쿼리 결과에 있는 농장만 필터링
            return criteriaBuilder.in(root.get("farmCode")).value(priceSubquery);
        };
    }


    // 청약 상태에 따라 농장을 필터링하는 스펙. subsStatus 0: 청약 예정, 1: 청약 진행 중, 2: 청약 종료
    public static Specification<Farm> filterBySubscriptionStatus(Integer subsStatus) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Subscription> subscriptionSubquery = query.subquery(Subscription.class);
            Root<Subscription> subscriptionRoot = subscriptionSubquery.from(Subscription.class);
            subscriptionSubquery.select(subscriptionRoot);

            // 연결 조건
            Predicate joinCondition = criteriaBuilder.equal(subscriptionRoot.get("farmCode"), root.get("farmCode"));

            // 상태 조건
            Predicate statusPredicate;
            if (subsStatus == 0) { // 청약 예정
                statusPredicate = criteriaBuilder.greaterThan(subscriptionRoot.get("startedTime"), criteriaBuilder.currentTimestamp());
            } else if (subsStatus == 1) { // 청약 진행 중
                statusPredicate = criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(subscriptionRoot.get("startedTime"), criteriaBuilder.currentTimestamp()),
                        criteriaBuilder.greaterThanOrEqualTo(subscriptionRoot.get("endedTime"), criteriaBuilder.currentTimestamp())
                );
            } else if (subsStatus == 2) { // 청약 종료
                statusPredicate = criteriaBuilder.lessThan(subscriptionRoot.get("endedTime"), criteriaBuilder.currentTimestamp());
            } else {
                statusPredicate = null;
            }

            return criteriaBuilder.exists(subscriptionSubquery.where(joinCondition, statusPredicate));
        };
    }


    // 관심 농장 여부를 확인하는 스펙
    public static Specification<Farm> hasInterest(int userCode) {
        return (root, query, criteriaBuilder) -> {
            Subquery<InterestFarm> interestSubquery = query.subquery(InterestFarm.class);
            Root<InterestFarm> interestRoot = interestSubquery.from(InterestFarm.class);
            interestSubquery.select(interestRoot);

            // 연결 조건
            Predicate joinCondition = criteriaBuilder.equal(interestRoot.get("farmCode"), root.get("farmCode"));

            // 사용자 코드 확인
            Predicate userCodePredicate = criteriaBuilder.equal(interestRoot.get("userCode"), userCode);

            return criteriaBuilder.exists(interestSubquery.where(joinCondition, userCodePredicate));
        };
    }


    // 농장 이름에 특정 키워드가 포함되어 있는지 확인하는 스펙
    public static Specification<Farm> nameContains(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("farmName"), "%" + keyword + "%");
    }


    // 원두 종류로 필터링하는 스펙
    public static Specification<Farm> filterByBeanType(String beanType) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("farmBeanName"), beanType);
    }


    // 요청된 정렬 기준에 따라 정렬하는 스펙. sort = null: 농장 이름 순, rate: 수익률 높은 순, deadline: 마감 기한 빠른 순,
    // highPrice: 공모가 높은 순으로 정렬, lowPrice: 공모가 낮은 순으로 정렬
    public static Specification<Farm> applySorting(String sort) {
        return (root, query, criteriaBuilder) -> {
            if ("rate".equals(sort)) {
                Root<Subscription> subscriptionRoot = query.from(Subscription.class);

                // 서브쿼리: 각 농장별로 가장 최근에 종료된 데이터의 endedTime 구하기
                Subquery<LocalDateTime> maxEndedTimeSubquery = query.subquery(LocalDateTime.class);
                Root<Subscription> maxEndedTimeRoot = maxEndedTimeSubquery.from(Subscription.class);
                maxEndedTimeSubquery.select(maxEndedTimeRoot.get("endedTime"))
                        .where(criteriaBuilder.and(
                                        criteriaBuilder.equal(maxEndedTimeRoot.get("farmCode"), root.get("farmCode")),
                                        criteriaBuilder.lessThan(maxEndedTimeRoot.get("endedTime"), LocalDateTime.now())),
                                criteriaBuilder.greaterThanOrEqualTo(maxEndedTimeRoot.get( "endedTime"), LocalDateTime.now().minusMonths(4))
                        );

                // 메인 쿼리: 서브쿼리에서 구한 가장 최근에 종료된 데이터에 해당하는 returnRate 선택
                Predicate joinCondition = criteriaBuilder.equal(subscriptionRoot.get("farmCode"), root.get("farmCode"));
                Predicate withinPeriod = criteriaBuilder.lessThan(subscriptionRoot.get("endedTime"), LocalDateTime.now());
                Predicate maxEndedTimeCondition = subscriptionRoot.get("endedTime").in(maxEndedTimeSubquery);
                query.where(criteriaBuilder.and(joinCondition, withinPeriod, maxEndedTimeCondition));

                query.orderBy(criteriaBuilder.desc(subscriptionRoot.get("returnRate")));



            } else if ("like".equals(sort)) {
                Subquery<Long> likeCountSubquery = query.subquery(Long.class);
                Root<InterestFarm> interestFarmRoot = likeCountSubquery.from(InterestFarm.class);
                likeCountSubquery.select(criteriaBuilder.count(interestFarmRoot.get("farmCode")))
                                .where(criteriaBuilder.equal(interestFarmRoot.get("farmCode"), root.get("farmCode")));

                query.orderBy(criteriaBuilder.desc(likeCountSubquery));
            } else if ("deadline".equals(sort)) {
                // 명시적으로 Subscription 테이블과 조인
                Root<Subscription> subscriptionRoot = query.from(Subscription.class);

                // 현재 시간
                LocalDateTime now = LocalDateTime.now();

                // 정렬 조건
                query.orderBy(criteriaBuilder.asc(subscriptionRoot.get("endedTime")));

                // 필터링된 농장만 조인
                Predicate joinCondition = criteriaBuilder.equal(subscriptionRoot.get("farmCode"), root.get("farmCode"));

                // 현재 시간이 startedTime과 endedTime 사이인 조건
                Predicate withinPeriod = criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(subscriptionRoot.get("startedTime"), now),
                        criteriaBuilder.greaterThanOrEqualTo(subscriptionRoot.get("endedTime"), now)
                );

                query.where(criteriaBuilder.and(joinCondition, withinPeriod));

            } else if ("highPrice".equals(sort) || "lowPrice".equals(sort)) {
                Subquery<Integer> priceSubquery = query.subquery(Integer.class);
                Root<Subscription> subscriptionRoot = priceSubquery.from(Subscription.class);
                if ("highPrice".equals(sort)) {
                    // 가격 높은 순으로 정렬
                    priceSubquery.select(criteriaBuilder.max(subscriptionRoot.get("confirmPrice")));
                    priceSubquery.where(
                            criteriaBuilder.equal(subscriptionRoot.get("farmCode"), root.get("farmCode"))
                    );
                    query.orderBy(criteriaBuilder.desc(priceSubquery));
                } else if ("lowPrice".equals(sort)) {
                    // 가격 낮은 순으로 정렬
                    priceSubquery.select(criteriaBuilder.min(subscriptionRoot.get("confirmPrice")));
                    priceSubquery.where(
                            criteriaBuilder.equal(subscriptionRoot.get("farmCode"), root.get("farmCode"))
                    );
                    query.orderBy(criteriaBuilder.asc(priceSubquery));
                }
            }
            else {
                // 기본 정렬 기준: 농장 이름 순
                query.orderBy(criteriaBuilder.asc(root.get("farmCode")));
            }
            return query.getRestriction();
        };
    }

    private static void orderByRecentSubscriptionReturnRate(Root<Farm> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        // 서브쿼리를 사용하여 가장 최근에 종료된 청약의 returnRate가 가장 높은 것을 조회
        Subquery<Double> subquery = query.subquery(Double.class);
        Root<Subscription> subscriptionRoot = subquery.from(Subscription.class);

        // 서브쿼리에서 가장 높은 returnRate를 선택하기 위해 최대 값을 사용
        Expression<Double> maxRate = criteriaBuilder.max(subscriptionRoot.get("returnRate"));

        subquery.select(maxRate)
                .where(
                        criteriaBuilder.equal(subscriptionRoot.get("farmCode"), root.get("farmCode")),
                        criteriaBuilder.lessThan(subscriptionRoot.get("endedTime"), LocalDateTime.now())
                );

        // 서브쿼리의 결과를 메인 쿼리에서 사용할 수 있도록 설정
        Expression<Double> rateExpression = subquery.getSelection();
        query.orderBy(criteriaBuilder.desc(rateExpression));
    }
}
