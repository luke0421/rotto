package com.rezero.rotto.repository;

import com.rezero.rotto.entity.FarmTop10;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmTop10Repository extends JpaRepository<FarmTop10, Integer> {
}
