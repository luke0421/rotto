package com.rezero.rotto.api.service;

import com.rezero.rotto.dto.dto.NoticeListDto;
import com.rezero.rotto.dto.response.NoticeDetailResponse;
import com.rezero.rotto.dto.response.NoticeListResponse;
import com.rezero.rotto.entity.Notice;
import com.rezero.rotto.entity.User;
import com.rezero.rotto.repository.NoticeRepository;
import com.rezero.rotto.repository.UserRepository;
import com.rezero.rotto.utils.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeServiceImpl implements NoticeService {

    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;
    private final Pagination pagination;


    // 공지사항 목록 조회
    public ResponseEntity<?> getNoticeList(int userCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 공지사항 모두 불러오기
        List<Notice> notices = noticeRepository.findAll();
        List<NoticeListDto> noticeListDtos = new ArrayList<>();

        // 최신것부터 보여주기 위해 리스트 뒤집기
        Collections.reverse(notices);
        // Notice 리스트를 순회
        for (Notice notice : notices) {
            // Dto 에 담기
            NoticeListDto noticeListDto = NoticeListDto.builder()
                    .noticeCode(notice.getNoticeCode())
                    .title(notice.getTitle())
                    .createTime(notice.getCreateTime())
                    .build();
            // noticeListDtos 에 담기
            noticeListDtos.add(noticeListDto);
        }

        // 리스폰스 생성
        NoticeListResponse response = NoticeListResponse.builder()
                .notices(noticeListDtos)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 공지사항 상세 조회
    public ResponseEntity<?> getNoticeDetail(int userCode, int noticeCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }
        // noticeCode 로 해당 공지사항 가져오기
        Notice notice = noticeRepository.findByNoticeCode(noticeCode);
        if (notice == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("공지사항을 찾을 수 없습니다.");
        }
        // 리스폰스 생성
        NoticeDetailResponse response = NoticeDetailResponse.builder()
                .title(notice.getTitle())
                .content(notice.getContent())
                .createTime(notice.getCreateTime())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
