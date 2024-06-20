package com.rezero.rotto.repository;

import com.rezero.rotto.entity.ApplyHistory;
import org.checkerframework.checker.units.qual.A;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ApplyHistoryRepository extends JpaRepository<ApplyHistory, Integer> {

    ApplyHistory findByUserCodeAndSubscriptionCode(int userCode, int subscriptionCode);

    // 유저가 신청, 취소한 내역 조회
    List<ApplyHistory> findByUserCodeAndIsDelete(int userCode, int isDelete);

    Optional<List<ApplyHistory>> findBySubscriptionCodeAndIsDelete(int subscriptionCode, int isDelete);
    ApplyHistory findByUserCodeAndSubscriptionCodeAndIsDelete(int userCode, int subscriptionCode, int isDelete);

    @Query(value = "SELECT SUM(applyCount) FROM ApplyHistory WHERE subscriptionCode = :subscriptionCode")
    Integer sumApplyCountBySubscriptionCode(int subscriptionCode);

    @Query(value = "SELECT SUM(applyCount) FROM ApplyHistory WHERE subscriptionCode = :subscriptionCode AND isDelete = 0")
    Integer sumApplyCountBySubscriptionCodeAndIsDelete(int subscriptionCode);
}
