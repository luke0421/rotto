package com.rezero.rotto.repository;

import com.rezero.rotto.entity.InterestFarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestFarmRepository extends JpaRepository<InterestFarm, Integer> {

    InterestFarm findByFarmCodeAndUserCode(int farmCode, int userCode);

    Long countByFarmCode(int farmCode);

}
