package com.rezero.rotto.api.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.rezero.rotto.dto.dto.ApplyHistoryListCancelDto;
import com.rezero.rotto.dto.dto.ApplyHistoryListGetDto;
import com.rezero.rotto.dto.response.ApplyHistoryListCancelResponse;
import com.rezero.rotto.dto.response.ApplyHistoryListGetResponse;
import com.rezero.rotto.entity.*;
import com.rezero.rotto.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplyHistoryServiceImpl implements ApplyHistoryService{

    private final ApplyHistoryRepository applyHistoryRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final FarmRepository farmRepository;
    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;


    @Override
    public ResponseEntity<?> postApply(int userCode, int subscriptionCode, int applyCount) {
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        ApplyHistory applyHistory = new ApplyHistory();
        ApplyHistory applyHistoryRepo = applyHistoryRepository.findByUserCodeAndSubscriptionCode(userCode, subscriptionCode);

        Subscription subscription = subscriptionRepository.findBySubscriptionCode(subscriptionCode);
        if (subscription == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("청약 정보가 없습니다.");
        }
        Account userAccount = accountRepository.findByUserCodeAndAccountType(userCode, 0);

        int applyBalance = subscription.getConfirmPrice() * applyCount;
        int limitNum = subscription.getLimitNum();
        LocalDateTime startedTime = subscription.getStartedTime();
        LocalDateTime endedTime = subscription.getEndedTime();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

        // 이미 신청한 청약이라면 안되지 ㅋ
        if (applyHistoryRepo != null && subscriptionCode == applyHistoryRepo.getSubscriptionCode()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 신청한 청약입니다.");
        }

        // 현재 시간이 시작 시간과 종료 시간 사이에 있는지 확인합니다.
        if (!now.isBefore(startedTime) && !now.isAfter(endedTime) && applyCount <= limitNum) {

            if (userAccount.getBalance() < applyCount * subscription.getConfirmPrice()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잔액이 부족합니다.");
            }

            // 날짜와 시간 포맷
            String formattedDate = now.format(dateFormatter);
            String formattedTime = now.format(timeFormatter);

            // 랜덤 6자리 일련번호 생성
            Random random = new Random();
            int randomNumber = random.nextInt(999999); // 0에서 999999까지의 난수 생성
            String formattedRandomNumber = String.format("%06d", randomNumber); // 난수를 6자리 문자열로 포맷팅

            // 기관 거래 고유 번호 생성
            String institutionTransactionUniqueNo = formattedDate + formattedTime + formattedRandomNumber;

            Map<String, Object> headerMap = new HashMap<>();
            headerMap.put("apiName", "accountTransfer");
            // 요청날짜
            headerMap.put("transmissionDate", formattedDate);
            headerMap.put("transmissionTime", formattedTime);
            // 기관코드 고정
            headerMap.put("institutionCode", "00100");
            //핀테크 앱 고정
            headerMap.put("fintechAppNo", "001");
            headerMap.put("apiServiceCode", "accountTransfer");
            // 기관 거래 고유 번호 : 새로운 번호로 임의 채번 (YYYYMMDD + HHMMSS + 일련번호 6자리) 또는 20자리의 난수
            headerMap.put("institutionTransactionUniqueNo", institutionTransactionUniqueNo);
            // 개발자 키
            headerMap.put("apiKey", "2afacf41e60a4482b5c4997d194a46f0");
            // 계정생성해야함 -> 회원가입시 이메일을 작성하면 생성됨
            headerMap.put("userKey", user.getUserKey());


            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("Header", headerMap);
            // 은행코드(은행이름으로 불리는 코드)
            bodyMap.put("depositBankCode", "001");
            bodyMap.put("depositAccountNo", "0015553944459869");
            bodyMap.put("depositTransactionSummary", "입금이체 계좌");
            bodyMap.put("transactionBalance", applyBalance);
            bodyMap.put("withdrawalBankCode", userAccount.getBankName());
            bodyMap.put("withdrawalAccountNo", userAccount.getAccountNum());
            bodyMap.put("withdrawalTransactionSummary", "출금이체 계좌");


            try {
                JsonNode jsonNode = WebClient.create("https://finapi.p.ssafy.io")
                        .post()
                        .uri("/ssafy/api/v1/edu/account/accountTransfer")
                        .bodyValue(bodyMap) // 구성한 Map을 bodyValue에 전달
                        .retrieve()
                        .bodyToMono(JsonNode.class)
                        .block();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }


            // 범위안에 있을 때
            applyHistory.setUserCode(userCode);
            applyHistory.setSubscriptionCode(subscriptionCode);
            applyHistory.setIsDelete(0);
            applyHistory.setApplyTime(now);
            applyHistory.setApplyCount(applyCount);
            applyHistoryRepository.save(applyHistory);


            AccountHistory accountHistory = new AccountHistory();
            accountHistory.setAccountCode(userAccount.getAccountCode());
            accountHistory.setAmount(applyBalance);
            accountHistory.setAccountTime(now);
            accountHistory.setDepositWithdrawalCode(2);
            accountHistoryRepository.save(accountHistory);


            return ResponseEntity.status(HttpStatus.OK).body(applyHistory);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("신청기간이 아닙니다.");
    }

    @Override
    public ResponseEntity<?> deleteApply(int userCode, int subscriptionCode) {
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        ApplyHistory applyHistory = applyHistoryRepository.findByUserCodeAndSubscriptionCode(userCode, subscriptionCode);
        if (applyHistory == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("신청내역이 없습니다.");
        }

        if (applyHistory.getIsDelete() == 1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 취소한 내역입니다.");
        }

        Subscription subscription = subscriptionRepository.findBySubscriptionCode(subscriptionCode);
        Account userAccount = accountRepository.findByUserCodeAndAccountType(userCode, 0);

        if (subscription == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("청약 정보가 없습니다.");
        }

        int applyCountBalance = subscription.getConfirmPrice() * applyHistory.getApplyCount();

        LocalDateTime startedTime = subscription.getStartedTime();
        LocalDateTime endedTime = subscription.getEndedTime();

        LocalDateTime now = LocalDateTime.now();

        // 현재 시간이 시작 시간과 종료 시간 사이에 있는지 확인합니다.
        if (!now.isBefore(startedTime) && !now.isAfter(endedTime)) {

            // 랜덤 6자리 일련번호 생성
            Random random = new Random();
            int randomNumber = random.nextInt(999999); // 0에서 999999까지의 난수 생성
            String formattedRandomNumber = String.format("%06d", randomNumber); // 난수를 6자리 문자열로 포맷팅

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

            // 날짜와 시간 포맷
            String formattedDate = now.format(dateFormatter);
            String formattedTime = now.format(timeFormatter);

            // 기관 거래 고유 번호 생성
            String institutionTransactionUniqueNo = formattedDate + formattedTime + formattedRandomNumber;

            Map<String, Object> headerMap = new HashMap<>();
            headerMap.put("apiName", "accountTransfer");
            // 요청날짜
            headerMap.put("transmissionDate", formattedDate);
            headerMap.put("transmissionTime", formattedTime);
            // 기관코드 고정
            headerMap.put("institutionCode", "00100");
            //핀테크 앱 고정
            headerMap.put("fintechAppNo", "001");
            headerMap.put("apiServiceCode", "accountTransfer");
            // 기관 거래 고유 번호 : 새로운 번호로 임의 채번 (YYYYMMDD + HHMMSS + 일련번호 6자리) 또는 20자리의 난수
            headerMap.put("institutionTransactionUniqueNo", institutionTransactionUniqueNo);
            // 개발자 키
            headerMap.put("apiKey", "2afacf41e60a4482b5c4997d194a46f0");
            //
            headerMap.put("userKey", "6c055b7e-b452-4e4a-ad20-b9533ed9e307");


            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("Header", headerMap);
            // 은행코드(은행이름으로 불리는 코드)
            bodyMap.put("depositBankCode", userAccount.getBankName());
            bodyMap.put("depositAccountNo", userAccount.getAccountNum());
            bodyMap.put("depositTransactionSummary", "입금이체 계좌");
            bodyMap.put("transactionBalance", applyCountBalance);
            // 관리자 계좌에서 빠져나감
            bodyMap.put("withdrawalBankCode", "001");
            bodyMap.put("withdrawalAccountNo", "0015553944459869");
            bodyMap.put("withdrawalTransactionSummary", "출금이체 계좌");

            try {
                JsonNode jsonNode = WebClient.create("https://finapi.p.ssafy.io")
                        .post()
                        .uri("/ssafy/api/v1/edu/account/accountTransfer")
                        .bodyValue(bodyMap) // 구성한 Map을 bodyValue에 전달
                        .retrieve()
                        .bodyToMono(JsonNode.class)
                        .block();

                // 범위안에 있을 때
                applyHistory.setIsDelete(1);
                applyHistoryRepository.save(applyHistory);

                AccountHistory accountHistory = new AccountHistory();
                accountHistory.setAccountCode(userAccount.getAccountCode());
                accountHistory.setAmount(applyCountBalance);
                accountHistory.setAccountTime(now);
                accountHistory.setDepositWithdrawalCode(1);
                accountHistoryRepository.save(accountHistory);

                return ResponseEntity.status(HttpStatus.OK).body(applyHistory);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("금융망 API 실패");
            }

        }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("신청기간이 아닙니다.");
    }

    @Override
    public ResponseEntity<?> getApply(int userCode) {
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        List<ApplyHistory> applyHistories = applyHistoryRepository.findByUserCodeAndIsDelete(userCode, 0);
        List<ApplyHistoryListGetDto> applyHistoryListDtos = new ArrayList<>();


        for (ApplyHistory applyHistory : applyHistories) {
            Subscription subscription = subscriptionRepository.findBySubscriptionCode(applyHistory.getSubscriptionCode());

            if (subscription == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("신청내역에 해당하는 청약내역이 없습니다.");
            }
            Farm farm = farmRepository.findByFarmCode(subscription.getFarmCode());

            if (farm == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("신청내역에 해당하는 청약의 농장정보가 없습니다.");
            }

            Integer totalApplyCount = applyHistoryRepository.sumApplyCountBySubscriptionCode(subscription.getSubscriptionCode());
            ApplyHistoryListGetDto applyHistoryListDto = ApplyHistoryListGetDto.builder()
                    .applyHistoryCode(applyHistory.getApplyHistoryCode())
                    .subscriptionCode(applyHistory.getSubscriptionCode())
                    .userCode(applyHistory.getUserCode())
                    .farmCode(farm.getFarmCode())
                    .farmName(farm.getFarmName())
                    .confirmPrice(subscription.getConfirmPrice())
                    .applyTime(applyHistory.getApplyTime())
                    .startedTime(subscription.getStartedTime())
                    .endedTime(subscription.getEndedTime())
                    .applyCount(applyHistory.getApplyCount())
                    .totalTokenCount(subscription.getTotalTokenCount())
                    .applyCount(applyHistory.getApplyCount())
                    .totalApplyCount(totalApplyCount)
                    .refundDate(subscription.getEndedTime().plusDays(1))
                    .build();

            applyHistoryListDtos.add(applyHistoryListDto);
        }

        ApplyHistoryListGetResponse response = ApplyHistoryListGetResponse.builder()
                .userApplyHistoryListGetDtos(applyHistoryListDtos)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<?> getApplyTerminated(int userCode) {
        User user = userRepository.findByUserCode(userCode);
        LocalDateTime now = LocalDateTime.now();
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        List<ApplyHistory> applyHistories = applyHistoryRepository.findByUserCodeAndIsDelete(userCode, 1);
        List<ApplyHistoryListCancelDto> applyHistoryListDtos = new ArrayList<>();


        for (ApplyHistory applyHistory : applyHistories) {
            Subscription subscription = subscriptionRepository.findBySubscriptionCode(applyHistory.getSubscriptionCode());
            Farm farm = farmRepository.findByFarmCode(subscription.getFarmCode());

            if (farm == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("신청내역에 해당하는 청약의 농장정보가 없습니다.");
            }
            ApplyHistoryListCancelDto applyHistoryListDto = ApplyHistoryListCancelDto.builder()
                    .subscriptionCode(applyHistory.getSubscriptionCode())
                    .userCode(applyHistory.getUserCode())
                    .farmCode(farm.getFarmCode())
                    .farmName(farm.getFarmName())
                    .confirmPrice(subscription.getConfirmPrice())
                    .applyTime(applyHistory.getApplyTime())
                    .startedTime(subscription.getStartedTime())
                    .endedTime(subscription.getEndedTime())
                    .isDelete(applyHistory.getIsDelete())
                    .applyCount(applyHistory.getApplyCount())
                    .totalTokenCount(subscription.getTotalTokenCount())
                    .build();

            applyHistoryListDtos.add(applyHistoryListDto);
        }

        ApplyHistoryListCancelResponse response = ApplyHistoryListCancelResponse.builder()
                .userApplyHistoryListCancelDtos(applyHistoryListDtos)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
