package com.rezero.rotto.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.rezero.rotto.dto.dto.SubscriptionListDto;
import com.rezero.rotto.dto.request.AccountDepositRequest;
import com.rezero.rotto.dto.request.CreateTokenRequest;
import com.rezero.rotto.dto.request.DistributeRequest;
import com.rezero.rotto.dto.request.PayTokensRequest;
import com.rezero.rotto.dto.request.RefundsTokenRequest;
import com.rezero.rotto.dto.response.SubscriptionDetailResponse;
import com.rezero.rotto.dto.response.SubscriptionListResponse;
import com.rezero.rotto.entity.Account;

import com.rezero.rotto.dto.request.SubscriptionProduceRequest;

import com.rezero.rotto.entity.AccountHistory;
import com.rezero.rotto.entity.ApplyHistory;
import com.rezero.rotto.entity.ExpenseDetail;
import com.rezero.rotto.entity.Farm;
import com.rezero.rotto.entity.Subscription;
import com.rezero.rotto.entity.TradeHistory;
import com.rezero.rotto.entity.User;
import com.rezero.rotto.repository.ApplyHistoryRepository;
import com.rezero.rotto.repository.FarmRepository;
import com.rezero.rotto.repository.SubscriptionRepository;
import com.rezero.rotto.repository.TradeHistoryRepository;
import com.rezero.rotto.repository.UserRepository;
import com.rezero.rotto.repository.*;
import com.rezero.rotto.utils.Pagination;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static com.rezero.rotto.utils.Const.VALID_BEAN_TYPES;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService{

    private final SubscriptionRepository subscriptionRepository;
    private final ApplyHistoryRepository applyHistoryRepository;
    private final UserRepository userRepository;
    private final FarmRepository farmRepository;
    private final BlockChainService blockChainService;
    private final TradeHistoryRepository tradeHistoryRepository;
    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;
    private final ExpenseDetailRepository expenseDetailRepository;
    private final Pagination pagination;

    private final Logger logger = LoggerFactory.getLogger(SubscriptionServiceImpl.class);

    public ResponseEntity<?> getSubscriptionList(int userCode, Integer page, Integer subsStatus, Integer minPrice, Integer maxPrice, String beanType, String sort, String keyword){
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 입력값 유효성 검사
        if (!isValidInput(subsStatus, minPrice, maxPrice, beanType)) {
            return ResponseEntity.badRequest().body("잘못된 입력값입니다.");
        }

        Specification<Subscription> spec = Specification.where(null);
        if (keyword != null) spec = spec.and(SubscriptionSpecification.nameContains(keyword));
        if (subsStatus != null) spec = spec.and(SubscriptionSpecification.filterBySubscriptionStatus(subsStatus));
        if (minPrice != null || maxPrice != null) spec = spec.and(SubscriptionSpecification.priceBetween(minPrice, maxPrice));
        if (beanType != null ) spec = spec.and(SubscriptionSpecification.filterByBeanType(beanType));
        spec = spec.and(SubscriptionSpecification.applySorting(sort));

        List<Subscription> subscriptions = subscriptionRepository.findAll(spec);
        // 인덱스 선언
        int startIdx = 0;
        int endIdx = 0;
        // 총 페이지 수 선언
        int totalPages = 1;

        // 페이지네이션
        List<Integer> indexes = pagination.pagination(page, 10, subscriptions.size());
        startIdx = indexes.get(0);
        endIdx = indexes.get(1);
        totalPages = indexes.get(2);

        // 페이지네이션
        List<Subscription> pageSubscriptions = subscriptions.subList(startIdx, endIdx);
        List<SubscriptionListDto> subscriptionListDtos = new ArrayList<>();

        for (Subscription subscription : pageSubscriptions) {
            Farm farm = farmRepository.findByFarmCode(subscription.getFarmCode());
            int subscriptionCode = subscription.getSubscriptionCode();
            Integer applyCount = applyHistoryRepository.sumApplyCountBySubscriptionCodeAndIsDelete(subscriptionCode);
            applyCount = (applyCount != null) ? applyCount : 0;

            // 현재 시각
            LocalDateTime now = LocalDateTime.now();

            // 시작 시간과 종료 시간
            LocalDateTime startedTime = subscription.getStartedTime();
            LocalDateTime endTime = subscription.getEndedTime();

            // substatus 값을 결정하는 로직
            int substatus;
            if (now.isBefore(startedTime)) {
                substatus = 0; // 현재 시각이 시작 시간보다 이전
            } else if (now.isAfter(endTime)) {
                substatus = 2; // 현재 시각이 종료 시간보다 이후
            } else {
                substatus = 1; // 현재 시각이 시작 시간과 종료 시간 사이
            }

            SubscriptionListDto subscriptionListDto = SubscriptionListDto.builder()
                    .subscriptionCode(subscriptionCode)
                    .farmCode(farm.getFarmCode())
                    .farmName(farm.getFarmName())
                    .confirmPrice(subscription.getConfirmPrice())
                    .applyCount(applyCount)
                    .startedTime(subscription.getStartedTime())
                    .endTime(subscription.getEndedTime())
                    .limitNum(subscription.getLimitNum())
                    .beanType(farm.getFarmBeanName())
                    .returnRate(subscription.getReturnRate())
                    .totalTokenCount(subscription.getTotalTokenCount())
                    .subsStatus(substatus)
                    .build();

            subscriptionListDtos.add(subscriptionListDto);
        }

        SubscriptionListResponse response = SubscriptionListResponse.builder()
                .subscriptions(subscriptionListDtos)
                .totalPages(totalPages)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    public ResponseEntity<?> getSubscriptionDetail(int userCode, int subscriptionCode){
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        Subscription subscriptionDetail = subscriptionRepository.findBySubscriptionCode(subscriptionCode);
        if (subscriptionDetail == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("청약내역이 없습니다.");
        }

        Integer applyCount = applyHistoryRepository.sumApplyCountBySubscriptionCodeAndIsDelete(subscriptionCode);
        ApplyHistory applyHistory1 = applyHistoryRepository.findByUserCodeAndSubscriptionCode(userCode, subscriptionCode);

        int isApplies = 0; // 청약 신청 여부 (0 : 신청하지 않음 or 신청 취소, 1 : 신청함) -> isDelete와 반대됨.
        if (applyHistory1 != null && subscriptionCode == applyHistory1.getSubscriptionCode()){
            isApplies = (applyHistory1.getIsDelete() + 1) % 2;
        }

        // 현재 시각
        LocalDateTime now = LocalDateTime.now();

        // 시작 시간과 종료 시간
        LocalDateTime startedTime = subscriptionDetail.getStartedTime();
        LocalDateTime endTime = subscriptionDetail.getEndedTime();

        // substatus 값을 결정하는 로직
        int substatus;
        if (now.isBefore(startedTime)) {
            substatus = 0; // 현재 시각이 시작 시간보다 이전
        } else if (now.isAfter(endTime)) {
            substatus = 2; // 현재 시각이 종료 시간보다 이후
        } else {
            substatus = 1; // 현재 시각이 시작 시간과 종료 시간 사이
        }

        applyCount = (applyCount != null) ? applyCount : 0;
        Farm farm = farmRepository.findByFarmCode(subscriptionDetail.getFarmCode());
        SubscriptionDetailResponse subscriptionDetailResponse = SubscriptionDetailResponse.builder()
                .subscriptionCode(subscriptionDetail.getSubscriptionCode())
                .farmCode(farm.getFarmCode())
                .farmName(farm.getFarmName())
                .confirmPrice(subscriptionDetail.getConfirmPrice())
                .startedTime(subscriptionDetail.getStartedTime())
                .endTime(subscriptionDetail.getEndedTime())
                .returnRate(subscriptionDetail.getReturnRate())
                .limitNum(subscriptionDetail.getLimitNum())
                .applyCount(applyCount)
                .totalTokenCount(subscriptionDetail.getTotalTokenCount())
                .subsStatus(substatus)
                .farmImg(farm.getFarmLogoPath())
                .isApply(isApplies)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(subscriptionDetailResponse);
    }


    // 청약시작 (딸각)
    public ResponseEntity<?> calculateSubscription(int subscriptionCode) {
        Subscription subscription = subscriptionRepository.findBySubscriptionCode(subscriptionCode);
        if(subscription == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 청약입니다.");
        }

        Optional<List<ApplyHistory>> applyHistories =
            applyHistoryRepository.findBySubscriptionCodeAndIsDelete(subscriptionCode, 0);

        if(applyHistories.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 청약의 신청자를 찾을 수 없습니다.");
        }

        // 총 신청토큰량이 발행토큰보다 적을때 바로 환불진행
        int totalApplyCount = applyHistoryRepository.sumApplyCountBySubscriptionCodeAndIsDelete(subscriptionCode);
        if (totalApplyCount < subscription.getTotalTokenCount()) {
            List<ApplyHistory> histories = applyHistories.get();
            for (ApplyHistory applyHistory : histories){
                 Subscription subscription1 = subscriptionRepository.findBySubscriptionCode(applyHistory.getSubscriptionCode());
                 User user = userRepository.findByUserCode(applyHistory.getUserCode());
                 Integer amount = applyHistory.getApplyCount() * subscription1.getConfirmPrice();
                 RefundMoney(user, amount);
            }

            // ROTTO burn
            blockChainService.burnToken(subscription);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("청약 자체가 취소되었습니다. 신청금액은 바로 환불됩니다.");
        }

        // 신청자마다 발급받을 ROTTO의 개수 파악
        HashMap<Integer, Integer> map = EqualDistribution(subscription, applyHistories.get());

        // 사용자의 지갑에 ROTTO 발급 (with. Smart Contract)
        for(ApplyHistory history : applyHistories.get()){
            int count = map.get(history.getUserCode()); // 발급받을 ROTTO 개수
            if(count == 0) continue;
            User user = userRepository.findByUserCode(history.getUserCode());
            String walletAddress = user.getBcAddress();
            DistributeRequest request = new DistributeRequest();
            request.setSubscription(subscription);
            request.setUserCode(user.getUserCode());
            request.setAmount(count);



            // ROTTO 발급 수행
            ResponseEntity<?> responseEntity = blockChainService.distributeTokens(request);
             if(responseEntity.getStatusCode() != HttpStatus.OK){
                return responseEntity;
             }

            // 받지 못한 토큰의 값어치만큼 환불
            int refundTokenCount = history.getApplyCount() - count;
            if(refundTokenCount > 0){ // 환불을 받아야 할 경우
                int amount = refundTokenCount * subscription.getConfirmPrice(); // 환불 금액
                if(!RefundMoney(user, amount)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("환불 과정에서 오류가 발생하였습니다.");
                }
            }
        }


        return ResponseEntity.ok().body("청약 ROTTO 발급 완료");
    }

    // 균등배분을 수행함.
    private HashMap<Integer, Integer> EqualDistribution(Subscription subscription, List<ApplyHistory> list) {
        Random random = new Random();
        Queue<ApplyHistory> queue = new ArrayDeque<>();
        HashMap<Integer, Integer> result = new HashMap<Integer, Integer>(); // userCode, 토큰 부여 개수
        for(ApplyHistory history : list){
            int userCode = history.getUserCode();
            result.put(userCode, 0);
            queue.offer(history);
        }

        int totalTokenCount = subscription.getTotalTokenCount();
        int limit = subscription.getLimitNum();

        // 총 발생 토큰 수보다 신청자 수가 더 많은 경우
        if(totalTokenCount < list.size()){
            List<ApplyHistory> randomList = new ArrayList<>(list);
            for(int i = 0; i < totalTokenCount; i++){
                int randomIndex = random.nextInt(randomList.size());
                ApplyHistory randomHistory = randomList.get(randomIndex);
                result.replace(randomHistory.getUserCode(), 1);
                randomList.remove(randomIndex);
            }
        }
        else {
            int currentCount = totalTokenCount;
            do{
                int size = queue.size();
                int NCount = currentCount / size;
                currentCount -= NCount * size;
                for(int i = 0; i < size; i++){
                    ApplyHistory history = queue.poll();
                    int applyCount = history.getApplyCount();
                    int temp = result.get(history.getUserCode()) + NCount; // 현재 개수 + n
                    int getTokens = Math.min(temp, applyCount);
                    result.replace(history.getUserCode(), getTokens);

                    if(getTokens < limit && applyCount > getTokens)   queue.offer(history);
                    if(temp > applyCount) currentCount += (temp - applyCount);
                }
            }while(!queue.isEmpty() && currentCount >= list.size());

            // 남은 토큰 개수 < 균등하게 배분해야 할 인원 수
            if(!queue.isEmpty()){
                List<ApplyHistory> randomList = new ArrayList<>(queue);
                for(int i = 0; i < currentCount; i++){
                    int randomIndex = random.nextInt(randomList.size());
                    ApplyHistory randomHistory = randomList.get(randomIndex);
                    result.replace(randomHistory.getUserCode(), result.get(randomHistory.getUserCode()) + 1);
                    randomList.remove(randomIndex);
                }
            }
        }

        return result;
    }


    private boolean isValidInput(Integer subsStatus, Integer minPrice, Integer maxPrice, String beanType) {

        // subsStatus 가 null 이 아니면서 0 미만이거나 2 초과다.
        if (subsStatus != null && (subsStatus < 0 || subsStatus > 2)) {
            return false;
        }
        // minPrice 와 maxPrice 가 모두 null 이 아니면서 minPrice 가 maxPrice 보다 크다.
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            return false;
        }

        // beanType 이 null 이 아니면서 허용 리스트에 포함되지 않는 값이면 false 처리
        return beanType == null || VALID_BEAN_TYPES.contains(beanType);
    }

    public ResponseEntity<?> postSubscription(int userCode, SubscriptionProduceRequest subscriptionProduceRequest) {
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // admin 값이 0이면
        if (!user.getAdmin()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("접근권한이 없습니다.");
        }

        // SubscriptionProduceRequest 유효성 검사
        if (subscriptionProduceRequest.getFarmCode() <= 0 ||
                subscriptionProduceRequest.getConfirmPrice() <= 0 ||
                subscriptionProduceRequest.getLimitNum() <= 0 ||
                // subscriptionProduceRequest.getReturnRate() <= 0 ||
                subscriptionProduceRequest.getPartnerFarmRate() <= 0 ||
                subscriptionProduceRequest.getTotalTokenCount() <= 0 ||
                subscriptionProduceRequest.getStartedTime() == null ||
                subscriptionProduceRequest.getEndedTime() == null ||
                subscriptionProduceRequest.getStartedTime().isAfter(subscriptionProduceRequest.getEndedTime())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다.");
        }

        Subscription subscription = new Subscription();
        subscription.setFarmCode(subscriptionProduceRequest.getFarmCode());
        subscription.setConfirmPrice(subscriptionProduceRequest.getConfirmPrice());
        subscription.setStartedTime(subscriptionProduceRequest.getStartedTime());
        subscription.setEndedTime(subscriptionProduceRequest.getEndedTime());
        subscription.setLimitNum(subscriptionProduceRequest.getLimitNum());
        subscription.setReturnRate(subscriptionProduceRequest.getReturnRate());
        subscription.setTotalTokenCount(subscriptionProduceRequest.getTotalTokenCount());
        subscription.setPartnerFarmRate(subscriptionProduceRequest.getPartnerFarmRate());
        subscriptionRepository.save(subscription);

        // 청약 ROTTO 생성
        CreateTokenRequest request = new CreateTokenRequest();
        request.setCode(subscription.getSubscriptionCode());
        request.setAmount(subscription.getTotalTokenCount());
        ResponseEntity<?> blockChainResponseEntity = blockChainService.createToken(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(subscription);
    }

    public ResponseEntity<?> refundSubscription(int userCode, int subscriptionCode) {
        // 관리자만 요청 가능
        boolean checkAdmin = userRepository.findByUserCode(userCode).getAdmin();
        if(!checkAdmin){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("요청 권한이 없습니다.");
        }

        Subscription subscription = subscriptionRepository.findBySubscriptionCode(subscriptionCode);
        if(subscription == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("청약을 찾을 수 없습니다.");
        }
        else if(subscription.getTotalSales() == 0){ // 총 판매액 입력이 안되어 있는 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("아직 경매가 완료되지 않은 청약입니다.");
        }
        else if(subscription.getTotalProceed() != 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 환급이 완료된 청약입니다.");
        }

        List<ExpenseDetail> expenseDetailList = expenseDetailRepository.findBySubscriptionCode(subscription.getSubscriptionCode());
        int TotalExpense = 0;
        for(ExpenseDetail details : expenseDetailList){
            TotalExpense += details.getExpenses();
        }

        // 경매가 - 농장 운영 비용
        logger.info("refundSubscription 시작");
        int price = subscription.getTotalSales() - TotalExpense;
        logger.info("[refundSubscription] price: " + price);
        int FarmIncome = (int)Math.ceil(price * (subscription.getPartnerFarmRate()) / 100.0); // 농장 수익
        logger.info("[refundSubscription] FarmIncome: " + FarmIncome);
        int fee = (int)Math.floor((price - FarmIncome) * 0.011);
        logger.info("[refundSubscription] 수수료: " + fee);
        int totalProceed = (price - FarmIncome) - fee; // 총 청약수익금액
        logger.info("[refundSubscription] totalProceed: " + totalProceed);

        subscription.setFee(fee);
        subscription.setTotalProceed(totalProceed);
        subscriptionRepository.save(subscription);

        double ROTTOprice = (double)totalProceed / subscription.getTotalTokenCount();
        logger.info("[refundSubscription] ROTTOprice: " + ROTTOprice);
        Optional<List<ApplyHistory>> applyHistories = applyHistoryRepository.findBySubscriptionCodeAndIsDelete(
            subscription.getSubscriptionCode(), 0);

        if(applyHistories.isPresent()) {
            for (ApplyHistory applyHistory : applyHistories.get()) {
                // 사업자 → 이용자 이체
                int applyCount = applyHistory.getApplyCount();
                User user = userRepository.findByUserCode(applyHistory.getUserCode());
                if(!RefundMoney(user, (int)Math.floor(ROTTOprice * applyCount))){
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("환급 중 에러 발생.");
                }

                // 이용자 지갑에 ROTTO 소각
                RefundsTokenRequest refundsTokenRequest = new RefundsTokenRequest();
                refundsTokenRequest.setCode(subscription.getSubscriptionCode());
                refundsTokenRequest.setAddress(user.getBcAddress());
                blockChainService.RefundsToken(refundsTokenRequest);

                // 거래장부 거래 내역 추가
                TradeHistory tradeHistory = new TradeHistory();
                tradeHistory.setUserCode(user.getUserCode());
                tradeHistory.setSubscriptionCode(subscription.getSubscriptionCode());
                tradeHistory.setTradeNum(applyCount);
                tradeHistory.setRefund(1);
                tradeHistory.setTokenPrice(ROTTOprice);
                tradeHistory.setBcAddress(user.getBcAddress());
                tradeHistoryRepository.save(tradeHistory);


            }
        }

        return ResponseEntity.ok().body("환급 완료");
    }

    // 사업자 → 이용자 가상계좌
    private boolean RefundMoney(User user, Integer amount) {
        // 이용자 가상계좌
        Account userRottoAccount = accountRepository.findByUserCodeAndAccountType(user.getUserCode(), 0);
        Account adminAccount = accountRepository.findByUserCodeAndAccountType(1, 0);

        if(userRottoAccount == null) return false; // 찾지 못함.

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

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
        headerMap.put("userKey", "b337eeaa-6e2d-4579-882f-a69b50676252");

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("Header", headerMap);
        // 은행코드(은행이름으로 불리는 코드)
        bodyMap.put("depositBankCode", userRottoAccount.getBankName());
        bodyMap.put("depositAccountNo", userRottoAccount.getAccountNum());
        bodyMap.put("depositTransactionSummary", "이용자 계좌");
        bodyMap.put("transactionBalance", String.valueOf(amount.intValue()));
        bodyMap.put("withdrawalBankCode", adminAccount.getBankName());
        bodyMap.put("withdrawalAccountNo", adminAccount.getAccountNum());
        bodyMap.put("withdrawalTransactionSummary", "관리자 계좌");

        try {
            JsonNode jsonNode =  WebClient.create("https://finapi.p.ssafy.io")
                .post()
                .uri("/ssafy/api/v1/edu/account/accountTransfer")
                .bodyValue(bodyMap) // 구성한 Map을 bodyValue에 전달
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block(); // error 발생

            // 입금 내역 저장
            AccountHistory accountHistory = new AccountHistory();
            accountHistory.setAccountCode(userRottoAccount.getAccountCode());
            accountHistory.setAmount(amount);
            accountHistory.setAccountTime(now);
            accountHistory.setDepositWithdrawalCode(1);
            accountHistoryRepository.save(accountHistory);

            // 출금 내역 저장
            AccountHistory adminAccountHistory = new AccountHistory();
            adminAccountHistory.setAccountCode(adminAccount.getAccountCode());
            adminAccountHistory.setAmount(amount);
            adminAccountHistory.setAccountTime(now);
            adminAccountHistory.setDepositWithdrawalCode(2);
            accountHistoryRepository.save(adminAccountHistory);

            // 입금
            userRottoAccount.setBalance(userRottoAccount.getBalance() + amount.intValue());
            accountRepository.save(userRottoAccount);

            adminAccount.setBalance(adminAccount.getBalance() - amount.intValue());
            accountRepository.save(adminAccount);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
