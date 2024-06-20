package com.rezero.rotto.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.rezero.rotto.entity.TradeHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeHistoryRepository extends JpaRepository<TradeHistory, Integer> {

    List<TradeHistory> findByUserCode(int userCode);
    List<TradeHistory> findByUserCodeAndRefund(int userCode, int refund);
    TradeHistory findByUserCodeAndSubscriptionCode(int userCode, int subscriptionCode);

    TradeHistory findByUserCodeAndSubscriptionCodeAndRefund(int userCode, int subscriptionCode, int refund);

}

