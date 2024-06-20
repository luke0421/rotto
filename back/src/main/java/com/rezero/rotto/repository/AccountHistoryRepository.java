package com.rezero.rotto.repository;

import com.rezero.rotto.entity.AccountHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Integer> {
    List<AccountHistory> findByAccountCode(int accountCode);
    List<AccountHistory> findByAccountCodeAndDepositWithdrawalCode(int accountCode, int depositWithdrawalCode);
}
