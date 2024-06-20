package com.rezero.rotto.repository;

import com.rezero.rotto.entity.Term;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsRepository extends JpaRepository<Term, Integer> {
}
