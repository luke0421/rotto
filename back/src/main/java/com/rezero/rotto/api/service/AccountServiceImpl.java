package com.rezero.rotto.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.rezero.rotto.dto.request.AccountConnectionRequest;
import com.rezero.rotto.dto.request.AccountDepositRequest;
import com.rezero.rotto.dto.request.AccountWithdrawalRequest;
import com.rezero.rotto.dto.response.AccountConnectionResponse;
import com.rezero.rotto.dto.response.AccountOneResponse;
import com.rezero.rotto.dto.response.AccountZeroResponse;
import com.rezero.rotto.entity.Account;
import com.rezero.rotto.entity.AccountHistory;
import com.rezero.rotto.entity.User;
import com.rezero.rotto.repository.AccountHistoryRepository;
import com.rezero.rotto.repository.AccountRepository;
import com.rezero.rotto.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService{

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;

    // rotto 계좌조회
    @Override
    public ResponseEntity<?> getAccountZero(int userCode) {
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

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
        headerMap.put("apiName", "inquireAccountBalance");
        // 요청날짜
        headerMap.put("transmissionDate", now.format(dateFormatter));
        headerMap.put("transmissionTime", now.format(timeFormatter));
        // 기관코드 고정
        headerMap.put("institutionCode", "00100");
        //핀테크 앱 고정
        headerMap.put("fintechAppNo", "001");
        headerMap.put("apiServiceCode", "inquireAccountBalance");
        // 기관 거래 고유 번호 : 새로운 번호로 임의 채번 (YYYYMMDD + HHMMSS + 일련번호 6자리) 또는 20자리의 난수
        headerMap.put("institutionTransactionUniqueNo", institutionTransactionUniqueNo);
        // 개발자 키
        headerMap.put("apiKey", "2afacf41e60a4482b5c4997d194a46f0");
        // 유저키
        headerMap.put("userKey", user.getUserKey());


        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("Header", headerMap);
        // 은행코드(은행이름으로 불리는 코드), 계좌번호
        System.out.println( accountRepository.findByUserCodeAndAccountType(userCode, 0).getBankName());
        bodyMap.put("bankCode", accountRepository.findByUserCodeAndAccountType(userCode, 0).getBankName());
        bodyMap.put("accountNo", accountRepository.findByUserCodeAndAccountType(userCode, 0).getAccountNum());

        try {
            JsonNode jsonNode = WebClient.create("https://finapi.p.ssafy.io")
                    .post()
                    .uri("/ssafy/api/v1/edu/account/inquireAccountBalance")
                    .bodyValue(bodyMap) // 구성한 Map을 bodyValue에 전달
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            System.out.println("금융망 계좌잔액조회 호출결과: " + jsonNode);

            // 'REC' 필드에 접근
            JsonNode recNode = jsonNode.get("REC");

            // 공모계좌 가져오기
            Account accountZero = accountRepository.findByUserCodeAndAccountType(userCode, 0);

            if (recNode != null) { // 'REC' 필드가 존재하는지 확인
                String accountBalance = recNode.get("accountBalance").asText();
                accountZero.setBalance(Integer.parseInt(accountBalance));
                accountRepository.save(accountZero);
            }

            AccountZeroResponse accountZeroResponse = AccountZeroResponse.builder()
                    .accountCode(accountZero.getAccountCode())
                    .bankName(accountZero.getBankName())
                    .accountHolder(user.getName())
                    .accountNum(accountZero.getAccountNum())
                    .accountBalance(accountZero.getBalance())
                    .accountType(accountZero.getAccountType())
                    .build();



            return ResponseEntity.status(HttpStatus.OK).body(accountZeroResponse);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<?> getAccountOne(int userCode) {
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }
        // 여기에 계좌 잔액 조회를 넣어서 저장해주기.
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
        headerMap.put("apiName", "inquireAccountBalance");
        // 요청날짜
        headerMap.put("transmissionDate", now.format(dateFormatter));
        headerMap.put("transmissionTime", now.format(timeFormatter));
        // 기관코드 고정
        headerMap.put("institutionCode", "00100");
        //핀테크 앱 고정
        headerMap.put("fintechAppNo", "001");
        headerMap.put("apiServiceCode", "inquireAccountBalance");
        // 기관 거래 고유 번호 : 새로운 번호로 임의 채번 (YYYYMMDD + HHMMSS + 일련번호 6자리) 또는 20자리의 난수
        headerMap.put("institutionTransactionUniqueNo", institutionTransactionUniqueNo);
        // 개발자 키
        headerMap.put("apiKey", "2afacf41e60a4482b5c4997d194a46f0");
        // 유저키
        headerMap.put("userKey", "ca55278a-89d2-4b51-bfa3-0cbbd376f9fd"); // 지정 계좌 하나 만들어야함.


        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("Header", headerMap);
        // 은행코드(은행이름으로 불리는 코드), 계좌번호
        bodyMap.put("bankCode", accountRepository.findByUserCodeAndAccountType(userCode, 1).getBankName());
        bodyMap.put("accountNo", accountRepository.findByUserCodeAndAccountType(userCode, 1).getAccountNum());

        try {
            JsonNode jsonNode = WebClient.create("https://finapi.p.ssafy.io")
                    .post()
                    .uri("/ssafy/api/v1/edu/account/inquireAccountBalance")
                    .bodyValue(bodyMap) // 구성한 Map을 bodyValue에 전달
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            System.out.println("금융망 진짜계좌잔액조회 호출결과: " + jsonNode);

            // 'REC' 필드에 접근
            JsonNode recNode = jsonNode.get("REC");

            // 공모계좌 가져오기
            Account accountOne = accountRepository.findByUserCodeAndAccountType(userCode, 1);

            if (recNode != null) { // 'REC' 필드가 존재하는지 확인
                String accountBalance = recNode.get("accountBalance").asText();
                accountOne.setBalance(Integer.parseInt(accountBalance));
                accountRepository.save(accountOne);
            }

            AccountOneResponse accountOneResponse = AccountOneResponse.builder()
                    .accountCode(accountOne.getAccountCode())
                    .bankName(accountOne.getBankName())
                    .accountHolder(user.getName())
                    .accountNum(accountOne.getAccountNum())
                    .accountBalance(accountOne.getBalance())
                    .accountType(accountOne.getAccountType())
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(accountOneResponse);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public ResponseEntity<?> postAccountConnection(int userCode, AccountConnectionRequest accountConnectionRequest) {
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 계좌와 연결되어 있는지 판단함.
        Account isConnected = accountRepository.findByUserCodeAndAccountType(userCode, 1);
        if(isConnected != null){
            return ResponseEntity.status(HttpStatus.FOUND).body("이미 계좌와 연결되어 있습니다.");
        }

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
        headerMap.put("apiName", "inquireAccountInfo");
        // 요청날짜
        headerMap.put("transmissionDate", formattedDate);
        headerMap.put("transmissionTime", formattedTime);
        // 기관코드 고정
        headerMap.put("institutionCode", "00100");
        //핀테크 앱 고정
        headerMap.put("fintechAppNo", "001");
        headerMap.put("apiServiceCode", "inquireAccountInfo");
        // 기관 거래 고유 번호 : 새로운 번호로 임의 채번 (YYYYMMDD + HHMMSS + 일련번호 6자리) 또는 20자리의 난수
        headerMap.put("institutionTransactionUniqueNo", institutionTransactionUniqueNo);
        // 개발자 키
        headerMap.put("apiKey", "2afacf41e60a4482b5c4997d194a46f0");
        // 계정생성해야함 -> 회원가입시 이메일을 작성하면 생성됨
        headerMap.put("userKey", "ca55278a-89d2-4b51-bfa3-0cbbd376f9fd");


        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("Header", headerMap);
        // 은행코드(은행이름으로 불리는 코드)
        bodyMap.put("bankCode", accountConnectionRequest.getBankName());
        bodyMap.put("accountNo", accountConnectionRequest.getAccountNum());

        System.out.println(bodyMap);
        try {
            JsonNode jsonNode =  WebClient.create("https://finapi.p.ssafy.io")
                    .post()
                    .uri("/ssafy/api/v1/edu/account/inquireAccountInfo")
                    .bodyValue(bodyMap) // 구성한 Map을 bodyValue에 전달
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            System.out.println("금융망 계좌조회(진짜계좌) 호출결과: " + jsonNode);

            // 'REC' 필드에 접근
            JsonNode recNode = jsonNode.get("REC");

            if (recNode != null) { // 'REC' 필드가 존재하는지 확인
                String bankCode = recNode.get("bankCode").asText();
                String accountNo = recNode.get("accountNo").asText();
                int accountBalance = Integer.parseInt( recNode.get("accountBalance").asText());
                Account account = new Account();
                account.setUserCode(userCode);
                account.setBankName(bankCode);
                account.setAccountNum(accountNo);
                account.setBalance(accountBalance);
                account.setAccountType(1);
                accountRepository.save(account);


                AccountConnectionResponse accountConnectionResponse = AccountConnectionResponse.builder()
                        .accountCode(account.getAccountCode())
                        .accountNum(account.getAccountNum())
                        .bankName(account.getBankName())
                        .accountType(account.getAccountType())
                        .build();

                return ResponseEntity.status(HttpStatus.OK).body(accountConnectionResponse);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("계좌연결에 실패했어요");
    }


    // 공모계좌에서 진짜계좌로 보내기.
    @Override
    public ResponseEntity<?> patchAccountWithdrawal(int userCode, AccountWithdrawalRequest accountWithdrawalRequest) {
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }
        int checkMoney = Integer.parseInt(accountWithdrawalRequest.getTransactionBalance());

        Account currentAccount = accountRepository.findByUserCodeAndAccountType(userCode, 0);
        Account toGoAccount = accountRepository.findByUserCodeAndAccountType(userCode, 1);

        if (checkMoney > currentAccount.getBalance()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잔액이 부족합니다.");
        }


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
        // 계정생성해야함 -> 회원가입시 이메일을 작성하면 생성됨
        headerMap.put("userKey", user.getUserKey());


        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("Header", headerMap);
        // 은행코드(은행이름으로 불리는 코드)
        bodyMap.put("depositBankCode", "002");
        bodyMap.put("depositAccountNo", "0025683504300707");
        bodyMap.put("depositTransactionSummary", "입금이체 계좌");
        bodyMap.put("transactionBalance", accountWithdrawalRequest.getTransactionBalance());
        bodyMap.put("withdrawalBankCode", currentAccount.getBankName());
        bodyMap.put("withdrawalAccountNo", currentAccount.getAccountNum());
        bodyMap.put("withdrawalTransactionSummary", "출금이체 계좌");


        try {
            JsonNode jsonNode =  WebClient.create("https://finapi.p.ssafy.io")
                    .post()
                    .uri("/ssafy/api/v1/edu/account/accountTransfer")
                    .bodyValue(bodyMap) // 구성한 Map을 bodyValue에 전달
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            System.out.println("금융망 계좌이체 호출결과: " + jsonNode);

            // 입출금내역 저장.
            AccountHistory accountHistory = new AccountHistory();
            accountHistory.setAccountCode(currentAccount.getAccountCode());
            accountHistory.setAmount(checkMoney);
            accountHistory.setAccountTime(now);
            accountHistory.setDepositWithdrawalCode(2);
            accountHistoryRepository.save(accountHistory);

            Account gongmoAccount = accountRepository.findByUserCodeAndAccountType(userCode, 0);
            gongmoAccount.setBalance(gongmoAccount.getBalance() - checkMoney);

            Account realAccount = accountRepository.findByUserCodeAndAccountType(userCode, 1);
            realAccount.setBalance(realAccount.getBalance() + checkMoney);

            accountRepository.save(gongmoAccount);
            accountRepository.save(realAccount);

            return ResponseEntity.status(HttpStatus.OK).body("이체 완료");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("이체에 실패했어요");
    }


    // 진짜계좌에서 공모계좌로 입금하기
    @Override
    public ResponseEntity<?> patchAccountDeposit(int userCode,  AccountDepositRequest accountDepositRequest) {
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        String transactionBalanceStr = accountDepositRequest.getTransactionBalance();
        if (transactionBalanceStr == null || transactionBalanceStr.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("입금액이 지정되지 않았습니다.");
        }

        int checkMoney;
        try {
            checkMoney = Integer.parseInt(transactionBalanceStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("입금액이 올바른 숫자 형식이 아닙니다.");
        }

        Account currentAccount = accountRepository.findByUserCodeAndAccountType(userCode, 1);
        Account toGoAccount = accountRepository.findByUserCodeAndAccountType(userCode, 0);

        if (checkMoney > currentAccount.getBalance()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잔액이 부족합니다.");
        }

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
        headerMap.put("userKey", "ca55278a-89d2-4b51-bfa3-0cbbd376f9fd");


        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("Header", headerMap);
        // 은행코드(은행이름으로 불리는 코드)
        bodyMap.put("depositBankCode", toGoAccount.getBankName());
        bodyMap.put("depositAccountNo", toGoAccount.getAccountNum());
        bodyMap.put("depositTransactionSummary", "입금이체 계좌");
        bodyMap.put("transactionBalance", transactionBalanceStr);
        bodyMap.put("withdrawalBankCode", "002");
        bodyMap.put("withdrawalAccountNo", "0025683504300707");
        bodyMap.put("withdrawalTransactionSummary", "출금이체 계좌");


        try {
            JsonNode jsonNode =  WebClient.create("https://finapi.p.ssafy.io")
                    .post()
                    .uri("/ssafy/api/v1/edu/account/accountTransfer")
                    .bodyValue(bodyMap) // 구성한 Map을 bodyValue에 전달
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            // 입출금내역 저장.
            AccountHistory accountHistory = new AccountHistory();
            accountHistory.setAccountCode(toGoAccount.getAccountCode());
            accountHistory.setAmount(checkMoney);
            accountHistory.setAccountTime(now);
            accountHistory.setDepositWithdrawalCode(1);
            accountHistoryRepository.save(accountHistory);

            Account gongmoAccount = accountRepository.findByUserCodeAndAccountType(userCode, 0);
            gongmoAccount.setBalance(gongmoAccount.getBalance() + checkMoney);

            Account realAccount = accountRepository.findByUserCodeAndAccountType(userCode, 1);
            realAccount.setBalance(realAccount.getBalance() - checkMoney);

            accountRepository.save(gongmoAccount);
            accountRepository.save(realAccount);

            return ResponseEntity.status(HttpStatus.OK).body("이체 완료");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("이체에 실패했어요");
    }


    @Override
    public ResponseEntity<?> deleteAccountConnection(int userCode, int accountCode) {
        // 유저가 존재하는지 확인
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        Account account = accountRepository.findByUserCodeAndAccountType(userCode, 1);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }

        if (account.getUserCode() != userCode) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }

        accountRepository.delete(account);
        return ResponseEntity.ok().body("계좌 삭제 완료");
    }
}
