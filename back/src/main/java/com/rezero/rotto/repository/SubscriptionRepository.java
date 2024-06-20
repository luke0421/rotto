package com.rezero.rotto.repository;

import com.rezero.rotto.entity.Subscription;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer>, JpaSpecificationExecutor<Subscription> {

    // 청약목록상세조회
    Subscription findBySubscriptionCode(int subscriptionCode);

    List<Subscription> findByFarmCode(int farmCode);

    @Query("SELECT s FROM Subscription s WHERE s.farmCode = :farmCode AND s.endedTime < :now ORDER BY s.endedTime DESC")
    List<Subscription> findLatestEndedSubscription(int farmCode, LocalDateTime now);

    @Query("SELECT s FROM Subscription s WHERE s.farmCode = :farmCode AND s.endedTime >= :now AND s.startedTime <= :now ORDER BY s.endedTime ASC")
    List<Subscription> findImpedingOngoingSubscription(int farmCode, LocalDateTime now);

    // 청약 시작일로 데이터를 찾는 메서드
    List<Subscription> findByStartedTime(LocalDateTime startDate);


}
