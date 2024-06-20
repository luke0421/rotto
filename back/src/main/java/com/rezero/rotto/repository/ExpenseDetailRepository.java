package com.rezero.rotto.repository;

import com.rezero.rotto.entity.ExpenseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseDetailRepository extends JpaRepository<ExpenseDetail, Integer> {

    // 청약 지출내역
    List<ExpenseDetail> findBySubscriptionCode(int subscriptionCode);

    @Query(value = "SELECT SUM(expenses) FROM ExpenseDetail WHERE subscriptionCode = :subscriptionCode")
    Integer sumExpenseDetailBySubscriptionCode(int subscriptionCode);
}
