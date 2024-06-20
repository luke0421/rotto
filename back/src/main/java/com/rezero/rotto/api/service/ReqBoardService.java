package com.rezero.rotto.api.service;

import com.rezero.rotto.dto.request.ReqRequest;
import org.springframework.http.ResponseEntity;

public interface ReqBoardService {

    ResponseEntity<?> getReqBoardList(int userCode);
    ResponseEntity<?> getReqBoardDetail(int userCode, int reqBoardCode);
    // 반환할 타입과 받을 값들
    ResponseEntity<?> postReqBoard(int userCode , ReqRequest reqRegisterBoard);
    ResponseEntity<?> updateReqBoard(int userCode, int reqBoardCode, ReqRequest reqUpdateBoard);
    ResponseEntity<?> deleteReqBoard(int userCode, int reqBoardCode);
}
