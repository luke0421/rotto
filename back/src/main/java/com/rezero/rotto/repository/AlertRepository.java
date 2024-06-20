package com.rezero.rotto.repository;

import com.rezero.rotto.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Integer> {

    Alert findByAlertCode(int alertCode);

    List<Alert> findByUserCode(int userCode);

    @Modifying
    @Transactional
    @Query("UPDATE Alert a Set a.isRead = true WHERE a.userCode = :userCode AND a.isRead = false")
    int markAllAlertAsReadByUserCode(int userCode);

    void deleteByUserCode(int userCode);

}
