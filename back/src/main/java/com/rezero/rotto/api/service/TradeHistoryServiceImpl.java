package com.rezero.rotto.api.service;

import com.rezero.rotto.dto.dto.TradeHistoryExpenseDetailOfSubDto;
import com.rezero.rotto.dto.dto.TradeHistoryHomeInfoDto;
import com.rezero.rotto.dto.dto.TradeHistoryListDto;
import com.rezero.rotto.dto.dto.TradeHistoryOwnListDto;
import com.rezero.rotto.dto.response.TradeHistoryExpenseDetailOfSubResponse;
import com.rezero.rotto.dto.response.TradeHistoryHomeInfoResponse;
import com.rezero.rotto.dto.response.TradeHistoryListResponse;
import com.rezero.rotto.dto.response.TradeHistoryOwnListResponse;
import com.rezero.rotto.entity.*;
import com.rezero.rotto.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TradeHistoryServiceImpl implements TradeHistoryService{

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final FarmRepository farmRepository;
    private final TradeHistoryRepository tradeHistoryRepository;
    private final ExpenseDetailRepository expenseDetailRepository;
    private final ApplyHistoryRepository applyHistoryRepository;

    public ResponseEntity<?> getTradeHistory(int userCode) {
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        List<TradeHistory> tradeHistories = tradeHistoryRepository.findByUserCodeAndRefund(userCode, 0);
        List<TradeHistoryListDto> tradeHistoryListDtos = new ArrayList<>();

        for (TradeHistory tradeHistory: tradeHistories) {
            Subscription subscription = subscriptionRepository.findBySubscriptionCode(tradeHistory.getSubscriptionCode());
            Farm farm =farmRepository.findByFarmCode(subscription.getFarmCode());

            TradeHistoryListDto tradeHistoryListDto = TradeHistoryListDto.builder()
                    .subscriptionCode(subscription.getSubscriptionCode())
                    .farmCode(farm.getFarmCode())
                    .farmName(farm.getFarmName())
                    .confirmPrice(subscription.getConfirmPrice())
                    .tradeTime(tradeHistory.getTradeTime())
                    .tradeNum(tradeHistory.getTradeNum())
                    .refund(tradeHistory.getRefund())
                    .totalTokenCount(subscription.getTotalTokenCount())
                    .build();

            tradeHistoryListDtos.add(tradeHistoryListDto);
        }

        TradeHistoryListResponse response = TradeHistoryListResponse.builder()
                .tradeHistoryListDtoss(tradeHistoryListDtos)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    public ResponseEntity<?> getTradeHistoryOwn(int userCode) {
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        List<TradeHistory> tradeHistories = tradeHistoryRepository.findByUserCodeAndRefund(userCode, 1);
        List<TradeHistoryOwnListDto> tradeHistoryOwnListDtos = new ArrayList<>();

        if (tradeHistories == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("정산내역이 없습니다.");
        }

        for (TradeHistory tradeHistory: tradeHistories) {
            Subscription subscription = subscriptionRepository.findBySubscriptionCode(tradeHistory.getSubscriptionCode());
            Farm farm =farmRepository.findByFarmCode(subscription.getFarmCode());

            TradeHistoryOwnListDto tradeHistoryListDto = TradeHistoryOwnListDto.builder()
                    .subscriptionCode(subscription.getSubscriptionCode())
                    .farmCode(farm.getFarmCode())
                    .farmName(farm.getFarmName())
                    .confirmPrice(subscription.getConfirmPrice())
                    .tradeTime(tradeHistory.getTradeTime())
                    .tradeNum(tradeHistory.getTradeNum())
                    .refund(tradeHistory.getRefund())
                    .totalTokenCount(subscription.getTotalTokenCount())
                    .proceed((int)Math.floor(tradeHistory.getTradeNum() * tradeHistory.getTokenPrice()))
                    .build();

            tradeHistoryOwnListDtos.add(tradeHistoryListDto);
        }

        TradeHistoryOwnListResponse response = TradeHistoryOwnListResponse.builder()
                .tradeHistoryOwnListDtoss(tradeHistoryOwnListDtos)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    public ResponseEntity<?> getExpenseDetailOfSub(int userCode, int subscriptionCode) {
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        Subscription subscription = subscriptionRepository.findBySubscriptionCode(subscriptionCode);
        TradeHistory tradeHistory = tradeHistoryRepository.findByUserCodeAndSubscriptionCodeAndRefund(userCode, subscriptionCode, 1);

        if (tradeHistory == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("신청내역이 없습니다.");
        }

        List<ExpenseDetail> expenseDetails = expenseDetailRepository.findBySubscriptionCode(subscriptionCode);
        Farm farm =farmRepository.findByFarmCode(subscription.getFarmCode());

        Integer sum = expenseDetailRepository.sumExpenseDetailBySubscriptionCode(subscriptionCode);
        int totalExpenses = (sum != null) ? sum.intValue() : 0;
        List<TradeHistoryExpenseDetailOfSubDto> tradeHistoryExpenseDetailOfSubDtos = new ArrayList<>();

        for (ExpenseDetail expenseDetail : expenseDetails ) {
            TradeHistoryExpenseDetailOfSubDto tradeHistoryExpenseDetailOfSubDto = TradeHistoryExpenseDetailOfSubDto.builder()
                    .expenditureContent(expenseDetail.getExpenditureContent())
                    .expenses(expenseDetail.getExpenses())
                    .build();

            tradeHistoryExpenseDetailOfSubDtos.add(tradeHistoryExpenseDetailOfSubDto);
        }

        TradeHistoryExpenseDetailOfSubResponse tradeHistoryExpenseDetailOfSubResponse =TradeHistoryExpenseDetailOfSubResponse.builder()
                .subscriptionCode(subscription.getSubscriptionCode())
                .farmName(farm.getFarmName())
                .farmCode(farm.getFarmCode())
                .totalSoldPrice(subscription.getTotalSales())
                .totalExpense(totalExpenses + subscription.getFee())
                .tradeTime(tradeHistory.getTradeTime())
                .tradeNum(tradeHistory.getTradeNum())
                .totalProceed(subscription.getTotalProceed())
                .myProceed((int)Math.floor(tradeHistory.getTradeNum() * tradeHistory.getTokenPrice()))
                .refund(tradeHistory.getRefund())
                .totalTokenCount(subscription.getTotalTokenCount())
                .tradeHistoryExpenseDetailOfSubDtoList(tradeHistoryExpenseDetailOfSubDtos)
                .fee(subscription.getFee())
                .build();



    return ResponseEntity.status(HttpStatus.OK).body(tradeHistoryExpenseDetailOfSubResponse);
    }

    public ResponseEntity<?> getHomeInvestInfo(int userCode) {
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 신청 총 금액
        List<ApplyHistory> applyHistories = applyHistoryRepository.findByUserCodeAndIsDelete(userCode, 0);
        int totalApplyAmount = 0;
        int totalApplyTokenNum = 0;
        for (ApplyHistory applyHistory : applyHistories) {
            Subscription subscription = subscriptionRepository.findBySubscriptionCode(applyHistory.getSubscriptionCode());
            totalApplyAmount += (subscription.getConfirmPrice() * applyHistory.getApplyCount());
            totalApplyTokenNum += applyHistory.getApplyCount();
        }

        // 총 보유 금액
        int totalOwnAmount = 0;
        int totalOwnTokenNum = 0;
        // 총 정산 금액
        int totalCalculateAmount = 0;
        int totalCalculateTokenNum = 0;

        Boolean flag = false;

        List<TradeHistory> tradeHistories = tradeHistoryRepository.findByUserCode(userCode);
        List<TradeHistoryHomeInfoDto> tradeHistoryHomeInfoDtos = new ArrayList<>();
        for (TradeHistory tradeHistory: tradeHistories) {
            Subscription subscription = subscriptionRepository.findBySubscriptionCode(tradeHistory.getSubscriptionCode());
            if (tradeHistory.getRefund() == 0){
                totalOwnAmount += (subscription.getConfirmPrice() * tradeHistory.getTradeNum());
                totalOwnTokenNum += tradeHistory.getTradeNum();
            } else if (tradeHistory.getRefund() == 1) {
                flag = true;
                totalCalculateAmount += (subscription.getConfirmPrice() * tradeHistory.getTradeNum());
                totalCalculateTokenNum += tradeHistory.getTradeNum();
            }
        }

        TradeHistoryHomeInfoDto tradeHistoryHomeInfoDto = TradeHistoryHomeInfoDto.builder()
                .title("투자 신청")
                .tokenNum(totalApplyTokenNum)
                .expenses(totalApplyAmount)
                .build();

        tradeHistoryHomeInfoDtos.add(tradeHistoryHomeInfoDto);

        TradeHistoryHomeInfoDto tradeHistoryHomeInfoDto1 = TradeHistoryHomeInfoDto.builder()
                .title("투자 보유")
                .tokenNum(totalOwnTokenNum)
                .expenses(totalOwnAmount)
                .build();

        tradeHistoryHomeInfoDtos.add(tradeHistoryHomeInfoDto1);

        TradeHistoryHomeInfoDto tradeHistoryHomeInfoDto2 = TradeHistoryHomeInfoDto.builder()
                .title("투자 정산")
                .tokenNum(totalCalculateTokenNum)
                .expenses(totalCalculateAmount)
                .build();
        tradeHistoryHomeInfoDtos.add(tradeHistoryHomeInfoDto2);


        int totalAmount = totalApplyAmount + totalOwnAmount + totalCalculateAmount;
        int totalProceedAmount = totalCalculateAmount - totalOwnAmount;
        double totalProceedPercent = 0;
        if (flag) {
            totalProceedPercent = ((double) (totalCalculateAmount - totalOwnAmount) / totalOwnAmount) * 100;
        }


        // 리스트 처리를 어떻게해야할까.
        TradeHistoryHomeInfoResponse response = TradeHistoryHomeInfoResponse.builder()
                .totalInvestAmount(totalAmount)
                .totalPercent(totalProceedPercent)
                .totalProceed(totalProceedAmount)
                .tradeHistoryHomeInfoDtoss(tradeHistoryHomeInfoDtos)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
