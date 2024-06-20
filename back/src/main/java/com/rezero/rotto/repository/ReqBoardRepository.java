package com.rezero.rotto.repository;

import com.rezero.rotto.entity.ReqBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReqBoardRepository extends JpaRepository<ReqBoard, Integer> {

    ReqBoard findByUserCodeAndReqBoardCode(int userCode, int reqBoardCode);
    List<ReqBoard> findByUserCode(int userCode);
}
