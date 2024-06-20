package com.rezero.rotto.api.service;

import com.rezero.rotto.dto.dto.ReqBoardListDto;
import com.rezero.rotto.dto.request.ReqRequest;
import com.rezero.rotto.dto.response.ReqBoardDetailRegisterModifyResponse;
import com.rezero.rotto.dto.response.ReqBoardListResponse;
import com.rezero.rotto.entity.ReqBoard;
import com.rezero.rotto.entity.User;
import com.rezero.rotto.repository.ReqBoardRepository;
import com.rezero.rotto.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReqBoardServiceImpl implements ReqBoardService {

    private final ReqBoardRepository reqBoardRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> getReqBoardList(int userCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        List<ReqBoard> reqBoardList = reqBoardRepository.findByUserCode(user.getUserCode());
        Collections.reverse(reqBoardList);
        List<ReqBoardListDto> reqBoardListDtos = new ArrayList<>();

        for (int i = 0; i < reqBoardList.size(); i++) {
            ReqBoardListDto reqBoardListDto = ReqBoardListDto.builder()
                    .reqBoardCode(reqBoardList.get(i).getReqBoardCode())
                    .title(reqBoardList.get(i).getTitle())
                    .createTime(reqBoardList.get(i).getCreateTime())
                    .build();
            reqBoardListDtos.add(reqBoardListDto);

        }

        ReqBoardListResponse reqBoardListResponse = ReqBoardListResponse.builder()
                .reqBoardListDtos(reqBoardListDtos)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(reqBoardListResponse);
    }

    public ResponseEntity<?> getReqBoardDetail(int userCode, int reqBoardCode) {
        User user = userRepository.findByUserCode(userCode);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        ReqBoard reqBoardDetail = reqBoardRepository.findByUserCodeAndReqBoardCode(user.getUserCode(), reqBoardCode);
        ReqBoardDetailRegisterModifyResponse reqBoardDetailResponse = ReqBoardDetailRegisterModifyResponse.builder()
                .reqBoardCode(reqBoardDetail.getReqBoardCode())
                .title(reqBoardDetail.getTitle())
                .contents(reqBoardDetail.getContent())
                .createTime(reqBoardDetail.getCreateTime())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(reqBoardDetailResponse);
    }

    @Override
    public ResponseEntity<?> postReqBoard(int userCode, ReqRequest reqRegisterBoard) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 'content' 필드에 대한 데이터 유효성 검사
        if (reqRegisterBoard.getContent() == null || reqRegisterBoard.getContent().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Content cannot be null or empty");
        }

        // ReqBoard 엔티티 생성 및 저장
        ReqBoard reqBoard = new ReqBoard();
        reqBoard.setTitle(reqRegisterBoard.getTitle());
        reqBoard.setContent(reqRegisterBoard.getContent());
        reqBoard.setUserCode(userCode); // User 엔티티 설정
        reqBoard.setCreateTime();
        reqBoardRepository.save(reqBoard);

        return ResponseEntity.status(HttpStatus.CREATED).body(reqBoard);
    }

    public ResponseEntity<?> updateReqBoard(int userCode, int reqBoardCode,
                                            ReqRequest updateData) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 해당 게시글이 존재하는지 검사
        ReqBoard reqBoard = reqBoardRepository.findByUserCodeAndReqBoardCode(user.getUserCode(), reqBoardCode);
        if (reqBoard == null) {
            return ResponseEntity.notFound().build();
        }

        // 게시글이 해당 사용자의 것인지 검사
        if (reqBoard.getUserCode() != userCode) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
        }

        // 'content' 필드에 대한 데이터 유효성 검사
        if (reqBoard.getContent() == null || reqBoard.getContent().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Content cannot be null or empty");
        }

        // 게시글 수정
        reqBoard.setTitle(updateData.getTitle());
        reqBoard.setContent(updateData.getContent());
        reqBoard.setCreateTime();
        reqBoardRepository.save(reqBoard);

        return ResponseEntity.ok().body(reqBoard);
    }

    public ResponseEntity<?> deleteReqBoard(int userCode, int reqBoardCode) {
        // 유저가 존재하는지 확인
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 게시글이 존재하는지 확인
        ReqBoard reqBoard = reqBoardRepository.findByUserCodeAndReqBoardCode(user.getUserCode(), reqBoardCode);
        if (reqBoard == null) {
            return ResponseEntity.notFound().build();
        }

        // 게시글이 해당 사용자의 것인지 확인
        if (reqBoard.getUserCode() != userCode) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }

        // 게시글 실제 삭제
        reqBoardRepository.delete(reqBoard);

        return ResponseEntity.ok().body("게시글 삭제 완료");
    }




}

