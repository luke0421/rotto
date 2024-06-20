package com.rezero.rotto.repository;

import com.rezero.rotto.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqReqository extends JpaRepository<Faq, Integer> {

    // 상세조회
    Faq findByFaqCode(int faqCode);
}
