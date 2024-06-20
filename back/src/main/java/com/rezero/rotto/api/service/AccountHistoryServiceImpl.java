package com.rezero.rotto.api.service;

import com.rezero.rotto.dto.dto.AccountHistoryListDto;
import com.rezero.rotto.dto.response.AccountHistoryListResponse;
import com.rezero.rotto.entity.AccountHistory;
import com.rezero.rotto.entity.User;
import com.rezero.rotto.repository.AccountHistoryRepository;
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
public class AccountHistoryServiceImpl implements AccountHistoryService{

    private final UserRepository userRepository;
    private final AccountHistoryRepository accountHistoryRepository;


    // 계좌 입출금 내역 조회
    public ResponseEntity<?> getAccountHistory(int userCode, int accountCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 계좌 내역 조회
        List<AccountHistory> accountHistoryList = accountHistoryRepository.findByAccountCode(accountCode);
        // 비어있으면 404 반환
        if (accountHistoryList == null || accountHistoryList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("계좌 내역이 없습니다.");
        }
        // 최신순 정렬을 위해 뒤집기
        Collections.reverse(accountHistoryList);
        // Dto 로 변환
        List<AccountHistoryListDto> accountHistoryListDtos = new ArrayList<>();
        for (AccountHistory accountHistory : accountHistoryList) {
            AccountHistoryListDto accountHistoryListDto = AccountHistoryListDto.builder()
                    .transferName(user.getName())
                    .amount(accountHistory.getAmount())
                    .accountTime(accountHistory.getAccountTime())
                    .depositOrWithdrawal(accountHistory.getDepositWithdrawalCode())
                    .build();
            accountHistoryListDtos.add(accountHistoryListDto);
        }

        // 리스폰스 생성
        AccountHistoryListResponse accountHistoryListResponse = AccountHistoryListResponse.builder()
                .accountHistoryListDtoss(accountHistoryListDtos)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(accountHistoryListResponse);
    }


    // 계좌 입금 내역 조회
    public ResponseEntity<?> getAccountHistoryDeposit(int userCode, int accountCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 계좌 입금 내역 조회
        List<AccountHistory> accountHistoryList = accountHistoryRepository.findByAccountCodeAndDepositWithdrawalCode(accountCode, 1);
        // 비어있으면 404 반환
        if (accountHistoryList == null || accountHistoryList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("입금 내역이 없습니다.");
        }
        // 최신순 정렬을 위해 뒤집기
        Collections.reverse(accountHistoryList);

        // Dto 로 변환
        List<AccountHistoryListDto> accountHistoryListDtos = new ArrayList<>();
        for (AccountHistory accountHistory : accountHistoryList) {
            AccountHistoryListDto accountHistoryListDto = AccountHistoryListDto.builder()
                    .transferName(user.getName())
                    .amount(accountHistory.getAmount())
                    .accountTime(accountHistory.getAccountTime())
                    .depositOrWithdrawal(accountHistory.getDepositWithdrawalCode())
                    .build();
            accountHistoryListDtos.add(accountHistoryListDto);
        }

        // 리스폰스 생성
        AccountHistoryListResponse accountHistoryListResponse = AccountHistoryListResponse.builder()
                .accountHistoryListDtoss(accountHistoryListDtos)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(accountHistoryListResponse);
    }


    // 계좌 출금 내역 조회
    public ResponseEntity<?> getAccountHistoryWithdrawal(int userCode, int accountCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 계좌 출금 내역 조회
        List<AccountHistory> accountHistoryList = accountHistoryRepository.findByAccountCodeAndDepositWithdrawalCode(accountCode, 2);
        // 비어있으면 404 반환
        if (accountHistoryList == null || accountHistoryList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("입금 내역이 없습니다.");
        }
        // 최신순 정렬을 위해 뒤집기
        Collections.reverse(accountHistoryList);

        // Dto 로 변환
        List<AccountHistoryListDto> accountHistoryListDtos = new ArrayList<>();
        for (int i = 0; i < accountHistoryList.size(); i++){
            AccountHistoryListDto accountHistoryListDto = AccountHistoryListDto.builder()
                    .transferName(user.getName())
                    .amount(accountHistoryList.get(i).getAmount())
                    .accountTime(accountHistoryList.get(i).getAccountTime())
                    .depositOrWithdrawal(accountHistoryList.get(i).getDepositWithdrawalCode())
                    .build();
            accountHistoryListDtos.add(accountHistoryListDto);
        }

        AccountHistoryListResponse accountHistoryListResponse = AccountHistoryListResponse.builder()
                .accountHistoryListDtoss(accountHistoryListDtos)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(accountHistoryListResponse);
    }
}
