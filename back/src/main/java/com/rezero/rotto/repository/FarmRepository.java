package com.rezero.rotto.repository;

import com.rezero.rotto.entity.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmRepository extends JpaRepository<Farm, Integer>, JpaSpecificationExecutor<Farm> {

    Farm findByFarmCode(int farmCode);

}
