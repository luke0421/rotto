package com.rezero.rotto.repository;

import com.rezero.rotto.entity.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeanRepository extends JpaRepository<Bean, Integer> {

    Bean findByBeanCode(int beanCode);

}
