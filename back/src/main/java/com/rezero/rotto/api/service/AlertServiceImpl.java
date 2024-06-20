package com.rezero.rotto.api.service;


import com.rezero.rotto.dto.dto.AlertListDto;
import com.rezero.rotto.dto.request.SseRequest;
import com.rezero.rotto.dto.response.AlertDetailResponse;
import com.rezero.rotto.dto.response.AlertListResponse;
import com.rezero.rotto.entity.Alert;
import com.rezero.rotto.entity.User;
import com.rezero.rotto.repository.AlertRepository;
import com.rezero.rotto.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final UserRepository userRepository;
    private final AlertRepository alertRepository;


    // 알림 목록 조회
    public ResponseEntity<?> getAlertList(int userCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 알림 모두 불러오기
        List<Alert> alertList = alertRepository.findByUserCode(userCode);
        // 최신것부터 보여주기 위해 리스트 뒤집기
        Collections.reverse(alertList);
        // stream 을 통해 alertList 를 순회하며 dto 리스트에 값을 담는다
        List<AlertListDto> alerts = alertList.stream()
                .map(alert -> new AlertListDto(alert.getAlertCode(), alert.getTitle(), alert.getAlertType(), alert.getCreateTime(), alert.getIsRead()))
                .toList();

        // 리스폰스 생성
        AlertListResponse response = new AlertListResponse(alerts);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 알림 상세 조회
    public ResponseEntity<?> getAlertDetail(int userCode, int alertCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 해당 알림이 존재하는지 검사
        Alert alert = alertRepository.findByAlertCode(alertCode);
        if (alert == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 알림이 존재하지 않습니다.");
        }
        
        // 해당 유저의 알림인지 검사
        if (alert.getUserCode() != userCode) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("타인의 알림에는 접근할 수 없습니다.");
        }

        if (!alert.getIsRead()) {
            // 읽지 않은 알림이면 읽음 처리
            alert.setIsRead(true);
        }

        // 리스폰스 생성
        AlertDetailResponse response = new AlertDetailResponse(alert.getTitle(), alert.getContent(), alert.getAlertType(), alert.getCreateTime());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 알림 삭제
    public ResponseEntity<?> deleteAlert(int userCode, int alertCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 해당 알림이 존재하는지 검사
        Alert alert = alertRepository.findByAlertCode(alertCode);
        if (alert == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 알림이 존재하지 않습니다.");
        }

        // 해당 유저의 알림인지 검사
        if (alert.getUserCode() != userCode) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("타인의 알림에는 접근할 수 없습니다.");
        }

        // 알림 삭제
        alertRepository.delete(alert);

        return ResponseEntity.status(HttpStatus.OK).body("삭제 성공");
    }


    // 알림 모두 읽음 처리
    public ResponseEntity<?> readAllAlert(int userCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 알림 모두 읽음 처리
        int updatedCount = alertRepository.markAllAlertAsReadByUserCode(userCode);

        return ResponseEntity.status(HttpStatus.OK).body(updatedCount + "개의 알림이 읽음 처리 되었습니다.");
    }


    // 알림 모두 삭제
    public ResponseEntity<?> deleteAllAlert(int userCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 알림 모두 삭제
        alertRepository.deleteByUserCode(userCode);
        
        return ResponseEntity.status(HttpStatus.OK).body("알림을 모두 삭제했습니다.");
    }


    // 알림 생성
    public boolean createAlert(SseRequest request) {
        int userCode = request.getUserCode();

        User user = userRepository.findByUserCode(userCode);
        // 존재하지 않는 유저이면 false 반환
        if (user == null) {
            return false;
        }
        // 알림 데이터 생성
        Alert alert = Alert.builder()
                .userCode(userCode)
                .title(request.getData().getTitle())
                .content(request.getData().getContent())
                .alertType(request.getName())
                .isRead(false)
                .build();

        alertRepository.save(alert);
        // 알림 생성후 true 반환
        return true;
    }
}
