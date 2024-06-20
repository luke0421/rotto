package com.rezero.rotto.repository;

import com.rezero.rotto.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {

    News findByNewsCode(int newsCode);

    Boolean existsByNewsDetailLink(String newsDetailLink);

}
