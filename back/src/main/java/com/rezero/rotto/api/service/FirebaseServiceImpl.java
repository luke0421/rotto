package com.rezero.rotto.api.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.rezero.rotto.dto.request.SendMessageToAllUsersRequest;
import com.rezero.rotto.dto.request.SendMessageToRequest;
import com.rezero.rotto.entity.User;
import com.rezero.rotto.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class FirebaseServiceImpl implements FirebaseService {

    private final UserRepository userRepository;
    /**
     * 특정 유저에게 푸시 알림을 전송하는 메서드
     * @param request token : FCM 토큰, title : 알림 제목, body : 알림 내용
     */
    public ResponseEntity<?> sendMessageTo(SendMessageToRequest request) {

        //알림 요청 받는 사람의 FCM Token 이 존재하는지 확인
        User user = userRepository.findByUserCode(request.getUserCode());
        String deviceToken = user.getDeviceToken();
        if (deviceToken == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("기기 정보가 존재하지 않습니다.");
        }

        // 전송 오류에 대한 예외 처리
        boolean check = sendMessage(deviceToken, request.getTitle(), request.getBody());
        if (!check) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 문제로 푸시 알림 전송에 실패하였습니다.");
        }

        return ResponseEntity.status(HttpStatus.OK).body("푸시 알림 전송 완료");
    }


    /**
     * FCM 토큰을 가진 모든 유저에게 푸시 알림을 전송하는 메서드
     * @param request title : 알림 제목, body : 알림 내용

     */
    public ResponseEntity<?> sendMessageToAllUsers(SendMessageToAllUsersRequest request) {
        // 모든 유저를 가져오기
        List<User> users = userRepository.findAll();
        // 성공과 실패를 카운팅하기 위해 정수 변수 정의
        int successCount = 0;
        int failureCount = 0;

        // 순회하며 알림 전송
        for (User user : users) {
            // 토큰을 가져오고 없으면 continue
            String deviceToken = user.getDeviceToken();
            if (deviceToken == null) {
                continue;
            }
            // 알림 생성
            Notification notification = Notification.builder()
                    .setTitle(request.getTitle())
                    .setBody(request.getBody())
                    .build();
            Message message = Message.builder()
                    .setToken(deviceToken)
                    .setNotification(notification)
                    .build();

            // 알림 전송
            try {
                String response = FirebaseMessaging.getInstance().send(message);
                log.info("Successfully sent message to user {}: {}", user.getName(), response);
                successCount++;
            } catch (FirebaseMessagingException e) {
                log.error("Error sending message to user {}: {}", user.getName(), e.getMessage());
                failureCount++;
            }
        }

        // 성공, 실패 개수를 전달
        String resultMessage = String.format("Successfully sent notifications to %d users, failed to send to %d users.", successCount, failureCount);
        return ResponseEntity.status(HttpStatus.OK).body(resultMessage);
    }


    public boolean sendMessage(String token, String title, String body) {
        //알림 요청 받는 사람의 FCM Token 이 존재하는지 확인
        if (!userRepository.existsByDeviceToken(token)) {
            return false;
        }

        try {
            // 알림 생성
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(notification)
                    .build();

            try {
                // 알림 전송
                FirebaseMessaging.getInstance().send(message);
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

}
