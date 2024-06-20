package com.rezero.rotto.repository;

import com.rezero.rotto.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    // 공모계좌 (accountType = 0), 연결계좌 조회(accountType = 1)
    Account findByUserCodeAndAccountType(int userCode, int accountType);
}
