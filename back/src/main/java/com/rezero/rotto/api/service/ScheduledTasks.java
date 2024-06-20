package com.rezero.rotto.api.service;

import com.rezero.rotto.entity.Alert;
import com.rezero.rotto.entity.ApplyHistory;
import com.rezero.rotto.entity.Subscription;
import com.rezero.rotto.entity.User;
import com.rezero.rotto.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

    private final SubscriptionRepository subscriptionRepository;
    private final ApplyHistoryRepository applyHistoryRepository;
    private final UserRepository userRepository;
    private final FarmRepository farmRepository;
    private final AlertRepository alertRepository;
    private final SseService sseService;
    private final FirebaseService firebaseService;


    /** 청약 시작일 오전 9시에 SSE 알림 보내기
     청약 시작일을 항상 09:00:00 으로 정의한다.
     **/
    @Scheduled(cron = "0 0 9 * * *")
    public void sendSubscriptionAlert() {
        log.info("Send subscription start alert");
        // 오늘 날짜 구하기
        LocalDateTime now = LocalDateTime.now().toLocalDate().atTime(9, 0, 0);

        // 청약 시작일이 오늘인 데이터 조회
        List<Subscription> subscriptions = subscriptionRepository.findByStartedTime(now);
        for (Subscription subscription : subscriptions) {
            // 청약 코드를 통해 해당 청약을 신청한 유저의 유저 코드 가져오기
            int subscriptionCode = subscription.getSubscriptionCode();
            // 청약 내역에서 삭제되지 않은 데이터를 가져옴
            Optional<List<ApplyHistory>> applyHistoriesOptional = applyHistoryRepository.findBySubscriptionCodeAndIsDelete(subscriptionCode, 0);
            // 청약 내역이 없으면 continue
            if (applyHistoriesOptional.isEmpty()) {
                continue;
            }
            List<ApplyHistory> applyHistories = applyHistoriesOptional.get();
            // 청약 내역 순회하면서 userCode 찾기
            for (ApplyHistory applyHistory : applyHistories) {
                // null 에 대한 예외 처리
                int userCode = applyHistory.getUserCode();
                User user = userRepository.findByUserCode(userCode);
                if (user == null) {
                    log.info("userCode: " + userCode + "를 가진 유저가 존재하지 않습니다.");
                    continue;
                }
                String deviceToken = user.getDeviceToken();
                if (deviceToken == null) {
                    log.info("userCode: " + userCode + "인 유저의 deviceToken 이 존재하지 않습니다.");
                    continue;
                }

                // 해당 유저에게 알림 보내기
                sseService.sendToClient(userCode, "subscription", "청약이 시작되었습니다. 청약 코드: " + subscriptionCode);

                // 데이터 만들어서 DB에 저장하기
                String farmName = farmRepository.findByFarmCode(subscription.getFarmCode()).getFarmName();
                String title = farmName + " 청약이 시작되었습니다.";
                String content = user.getName() + "님이 신청하신 " + farmName + "의 청약이 시작되었습니다.";
                String alertType = "알림";

                // Firebase 푸시 알림 보내기
                firebaseService.sendMessage(deviceToken, title, content);

                // Firebase 푸시 알림 보내기
                boolean check = firebaseService.sendMessage(deviceToken, title, content);
                // 푸시 알림 전송 실패시 continue
                if (!check) {
                    continue;
                }

                // DB에 알림 저장
                Alert alert = Alert.builder()
                        .userCode(userCode)
                        .title(title)
                        .content(content)
                        .alertType(alertType)
                        .build();

                alertRepository.save(alert);

                log.info("Successfully sent notification to user with userCode: " + userCode + ", subscriptionCode: " + subscriptionCode);
            }
        }
    }


    /** 청약 시작 하루 전 오전 9시에 SSE 알림 보내기
     청약 시작일을 항상 09:00:00 으로 정의한다.
     **/
    @Scheduled(cron = "0 0 9 * * *")
    public void sendSubscriptionAlertOneDayBefore() {
        log.info("Send a alert a Day Before the subscription starts");
        // 내일 날짜 구하기
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow9AM = now.toLocalDate().plusDays(1).atTime(9, 0, 0);


        // 청약 시작 하루 전인 데이터 조회
        List<Subscription> subscriptions = subscriptionRepository.findByStartedTime(tomorrow9AM);
        for (Subscription subscription : subscriptions) {
            // 청약 코드를 통해 해당 청약을 신청한 유저의 유저 코드 가져오기
            int subscriptionCode = subscription.getSubscriptionCode();
            // 청약 내역에서 삭제되지 않은 데이터를 가져옴
            Optional<List<ApplyHistory>> applyHistoriesOptional = applyHistoryRepository.findBySubscriptionCodeAndIsDelete(subscriptionCode, 0);
            // 청약 내역이 없으면 continue
            if (applyHistoriesOptional.isEmpty()) {
                continue;
            }
            List<ApplyHistory> applyHistories = applyHistoriesOptional.get();
            // 청약 내역 순회하면서 userCode 찾기
            for (ApplyHistory applyHistory : applyHistories) {
                // null 에 대한 예외 처리
                int userCode = applyHistory.getUserCode();
                User user = userRepository.findByUserCode(userCode);
                if (user == null) {
                    log.info("userCode: " + userCode + "를 가진 유저가 존재하지 않습니다.");
                    continue;
                }
                String deviceToken = user.getDeviceToken();
                if (deviceToken == null) {
                    log.info("userCode: " + userCode + "인 유저의 deviceToken 이 존재하지 않습니다.");
                    continue;
                }

                // 해당 유저에게 알림 보내기
                sseService.sendToClient(userCode, "subscription-1", "청약 시작 하루전입니다. 청약 코드: " + subscriptionCode);

                // 데이터 만들어서 DB에 저장하기
                String farmName = farmRepository.findByFarmCode(subscription.getFarmCode()).getFarmName();
                String title =  farmName + " 청약 시작 하루 전입니다.";
                String content = user.getName() + "님이 신청하신 " + farmName + "의 청약 시작이 하루 남았습니다.";
                String alertType = "알림";

                // Firebase 푸시 알림 보내기
                boolean check = firebaseService.sendMessage(deviceToken, title, content);
                // 푸시 알림 전송 실패시 continue
                if (!check) {
                    continue;
                }

                // DB에 알림 저장
                Alert alert = Alert.builder()
                        .userCode(userCode)
                        .title(title)
                        .content(content)
                        .alertType(alertType)
                        .build();

                alertRepository.save(alert);

                log.info("Successfully sent notification to user with userCode: " + userCode + ", subscriptionCode: " + subscriptionCode);
            }
        }
    }


    /** 청약 시작 3일 전 오전 9시에 SSE 알림 보내기
     청약 시작일을 항상 09:00:00 으로 정의한다.
     **/
    @Scheduled(cron = "0 0 9 * * *")
    public void sendSubscriptionAlertThreeDaysBefore() {
        log.info("Send a alert 3 Days Before the subscription starts");
        // 3일 후 날짜 구하기
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeDaysLaterAt9AM = now.toLocalDate().plusDays(3).atTime(9, 0, 0);


        // 청약 시작 3일 전인 데이터 조회
        List<Subscription> subscriptions = subscriptionRepository.findByStartedTime(threeDaysLaterAt9AM);
        for (Subscription subscription : subscriptions) {
            // 청약 코드를 통해 해당 청약을 신청한 유저의 유저 코드 가져오기
            int subscriptionCode = subscription.getSubscriptionCode();
            // 청약 내역에서 삭제되지 않은 데이터를 가져옴
            Optional<List<ApplyHistory>> applyHistoriesOptional = applyHistoryRepository.findBySubscriptionCodeAndIsDelete(subscriptionCode, 0);
            // 청약 내역이 없으면 continue
            if (applyHistoriesOptional.isEmpty()) {
                continue;
            }
            List<ApplyHistory> applyHistories = applyHistoriesOptional.get();
            // 청약 내역 순회하면서 userCode 찾기
            for (ApplyHistory applyHistory : applyHistories) {
                // null 에 대한 예외 처리
                int userCode = applyHistory.getUserCode();
                User user = userRepository.findByUserCode(userCode);
                if (user == null) {
                    log.info("userCode: " + userCode + "를 가진 유저가 존재하지 않습니다.");
                    continue;
                }
                String deviceToken = user.getDeviceToken();
                if (deviceToken == null) {
                    log.info("userCode: " + userCode + "인 유저의 deviceToken 이 존재하지 않습니다.");
                    continue;
                }

                // 해당 유저에게 알림 보내기
                sseService.sendToClient(userCode, "subscription-3", "청약 시작 3일 전입니다. 청약 코드: " + subscriptionCode);

                // 데이터 만들어서 DB에 저장하기
                String farmName = farmRepository.findByFarmCode(subscription.getFarmCode()).getFarmName();

                String title =  farmName + " 청약 시작 3일 전입니다.";
                String content = user.getName() + "님이 신청하신 " + farmName + "의 청약 시작이 3일 남았습니다.";
                String alertType = "알림";

                // Firebase 푸시 알림 보내기
                firebaseService.sendMessage(deviceToken, title, content);

                // Firebase 푸시 알림 보내기
                boolean check = firebaseService.sendMessage(deviceToken, title, content);
                // 푸시 알림 전송 실패시 continue
                if (!check) {
                    continue;
                }

                Alert alert = Alert.builder()
                        .userCode(userCode)
                        .title(title)
                        .content(content)
                        .alertType(alertType)
                        .build();

                alertRepository.save(alert);

                log.info("Successfully sent notification to user with userCode: " + userCode + ", subscriptionCode: " + subscriptionCode);
            }
        }
    }


    /** 청약 시작 일주일 전 오전 9시에 SSE 알림 보내기
     청약 시작일을 항상 09:00:00 으로 정의한다.
     **/
    @Scheduled(cron = "0 0 9 * * *")
    public void sendSubscriptionAlertOneWeekBefore() {
        log.info("Send a alert a Week Before the subscription starts");
        // 일주일 후 날짜 구하기
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekLaterAt9AM = now.plusWeeks(1).withHour(9).withMinute(0).withSecond(0).withNano(0);

        // 청약 시작 일주일 전인 청약 조회
        List<Subscription> subscriptions = subscriptionRepository.findByStartedTime(oneWeekLaterAt9AM);
        for (Subscription subscription : subscriptions) {
            // 청약 코드를 통해 해당 청약을 신청한 유저의 유저 코드 가져오기
            int subscriptionCode = subscription.getSubscriptionCode();
            // 청약 내역에서 삭제되지 않은 데이터를 가져옴
            Optional<List<ApplyHistory>> applyHistoriesOptional = applyHistoryRepository.findBySubscriptionCodeAndIsDelete(subscriptionCode, 0);
            // 청약 내역이 없으면 continue
            if (applyHistoriesOptional.isEmpty()) {
                continue;
            }
            List<ApplyHistory> applyHistories = applyHistoriesOptional.get();
            // 청약 내역 순회하면서 userCode 찾기
            for (ApplyHistory applyHistory : applyHistories) {
                // null 에 대한 예외 처리
                int userCode = applyHistory.getUserCode();
                User user = userRepository.findByUserCode(userCode);
                if (user == null) {
                    log.info("userCode: " + userCode + "를 가진 유저가 존재하지 않습니다.");
                    continue;
                }
                String deviceToken = user.getDeviceToken();
                if (deviceToken == null) {
                    log.info("userCode: " + userCode + "인 유저의 deviceToken 이 존재하지 않습니다.");
                    continue;
                }

                // 해당 유저에게 알림 보내기
                sseService.sendToClient(userCode, "subscription-7", "청약 시작 일주일 전입니다. 청약 코드: " + subscriptionCode);

                // 데이터 만들어서 DB에 저장하기
                String farmName = farmRepository.findByFarmCode(subscription.getFarmCode()).getFarmName();
                String title =  farmName + " 청약 시작 일주일 전입니다.";
                String content = user.getName() + "님이 신청하신 " + farmName + "의 청약 시작이 일주일 남았습니다.";
                String alertType = "알림";

                // Firebase 푸시 알림 보내기
                firebaseService.sendMessage(deviceToken, title, content);

                // Firebase 푸시 알림 보내기
                boolean check = firebaseService.sendMessage(deviceToken, title, content);
                // 푸시 알림 전송 실패시 continue
                if (!check) {
                    continue;
                }

                Alert alert = Alert.builder()
                        .userCode(userCode)
                        .title(title)
                        .content(content)
                        .alertType(alertType)
                        .build();

                alertRepository.save(alert);

                log.info("Successfully sent notification to user with userCode: " + userCode + ", subscriptionCode: " + subscriptionCode);
            }
        }
    }
}
