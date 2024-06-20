package com.rezero.rotto.api.service;

import com.rezero.rotto.dto.dto.AlertDto;
import com.rezero.rotto.dto.request.SseRequest;
import com.rezero.rotto.entity.Alert;
import com.rezero.rotto.entity.ApplyHistory;
import com.rezero.rotto.entity.Subscription;
import com.rezero.rotto.entity.User;
import com.rezero.rotto.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SseServiceImpl implements SseService {

    private final EmitterRepository emitterRepository;

    
    // SSE 구독 설정
    public SseEmitter subscribe(int userCode) {
        // 1시간 타임 아웃 설정
        long TIME_OUT = 60 * 60 * 1000L;
        SseEmitter sseEmitter = new SseEmitter(TIME_OUT);
        // SseEmitter 저장
        sseEmitter = emitterRepository.save(userCode, sseEmitter);

        // SSE 완료 이벤트 리스너 설정
        sseEmitter.onCompletion(() -> {
            log.info("disconnected by complete server sent event : id={}", userCode);
        });
        // SSE 타임아웃 이벤트 리스너 설정
        sseEmitter.onTimeout(() -> {
            log.info("server sent event timed out : id={}", userCode);
        });
        // SSE 에러 이벤트 리스너 설정
        sseEmitter.onError((e) -> {
            log.info("server sent event error occurred : id={}, message={}", userCode, e.getMessage());
            emitterRepository.deleteByUserCode(userCode); // 에러 발생 시 해당 사용자 코드로 저장된 Emitter 삭제
        });

        // 클라이언트에 연결 메시지 전송
        sendToClient(userCode, "connect", "SSE connected");

        return sseEmitter;
    }


    // 클라이언트로 데이터 전송
    public void sendToClient(int userCode, String name, Object data) {
        SseEmitter sseEmitter = emitterRepository.findByUserCode(userCode);

        if (sseEmitter != null) {
            try {
                log.info("send to client {}:[{}]", name, data);
                sseEmitter.send(SseEmitter.event()
                        .id(String.valueOf(userCode))
                        .name(name)
                        .data(data, MediaType.APPLICATION_JSON)); // JSON 형태로 데이터 전송
            } catch (IOException e) {
                log.error("failure to send event, id={}, message={}", userCode, e.getMessage());
                emitterRepository.deleteByUserCode(userCode); // 전송 실패 시 Emitter 삭제
            }
        }
    }


    // SSE 연결 종료
    public void disConnect(int userCode) {
        sendToClient(userCode, "disconnect", "SSE disconnected"); // 클라이언트에 연결 종료 메시지 전송
        emitterRepository.deleteByUserCode(userCode); // Emitter 삭제
    }
}
