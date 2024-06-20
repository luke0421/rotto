package com.rezero.rotto.repository;

import com.rezero.rotto.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    Notice findByNoticeCode(int noticeCode);

}
