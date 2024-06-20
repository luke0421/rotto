package com.rezero.rotto.api.service;

//import com.rezero.rotto.dto.request.RegisterPinRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.Http;
import com.rezero.rotto.dto.request.*;
import com.rezero.rotto.dto.response.CheckEmailResponse;
import com.rezero.rotto.dto.response.CheckPhoneNumResponse;
import com.rezero.rotto.dto.response.UserInfoResponse;
import com.rezero.rotto.entity.Account;
import com.rezero.rotto.entity.User;
import com.rezero.rotto.repository.AccountRepository;
import com.rezero.rotto.repository.UserRepository;
import com.rezero.rotto.utils.AESUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.web3j.crypto.WalletUtils;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecretKey aesKey;
    private final AccountRepository accountRepository;
    private final BlockChainService blockChainService;

    // 회원가입
    public ResponseEntity<?> signUp(SignUpRequest request) {
        try {
            // 휴대폰 번호 암호화
            String encryptedPhoneNum = AESUtil.encrypt(request.getPhoneNum(), aesKey);
            // 주민번호 암호화
            String encryptedJuminNo = AESUtil.encrypt(request.getJuminNo(), aesKey);
            // 패스워드 해쉬화
            String hashedPassword = passwordEncoder.encode(request.getPassword());

            // 이미 존재하는 휴대폰 번호로 가입을 시도할 경우 예외 처리
            if (userRepository.existsByPhoneNum(encryptedPhoneNum)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 휴대폰 번호입니다.");
            }

            // 이메일을 받고 금융망 API 와 연결하여 금융망 계정 생성
            String userEmail = request.getEmail();
            JsonNode jsonNode = WebClient.create("https://finapi.p.ssafy.io")
                    .post()
                    .uri("/ssafy/api/v1/member/")
                    .bodyValue( new CreateFinanceAccountRequest("2afacf41e60a4482b5c4997d194a46f0", userEmail))
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            // 'userKey' 값을 추출
            String userKeyOfFinance = jsonNode.path("payload").path("userKey").asText();

            // userCode 자동, isDelete 기본값 0, joinDate = CreationTimestamp, deleteTime = null
            User user = User.builder()
                    .name(request.getName())
                    .sex(request.getSex())
                    .phoneNum(encryptedPhoneNum)
                    .juminNo(encryptedJuminNo)
                    .password(hashedPassword)
                    .email(request.getEmail())
                    .userKey(userKeyOfFinance)
                    .build();

            // 저장
            userRepository.save(user);

            // 계좌생성
            financeAccountCreate(user.getUserCode(), userKeyOfFinance);

            // 리스폰스 생성
            UserInfoResponse response = UserInfoResponse.builder()
                    .userCode(user.getUserCode())
                    .name(user.getName())
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 실패");
        }
    }


    // 폰번호 중복 체크
    public ResponseEntity<?> checkPhoneNum(CheckPhoneNumRequest request) {
        try {
            // 요청받은 폰번호를 암호화
            String encryptedPhoneNum = AESUtil.encrypt(request.getPhoneNum(), aesKey);
            // 암호화된 폰번호를 DB 에서 조회하여 데이터가 존재하는지를 Bool 형태로 체크
            Boolean isExist = userRepository.existsByPhoneNum(encryptedPhoneNum);
            // 리스폰스 생성후 반환
            CheckPhoneNumResponse response = new CheckPhoneNumResponse(isExist);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("중복체크 실패");
        }
    }


    // 이메일 중복 체크
    public ResponseEntity<?> checkEmail(CheckEmailRequest request) {
        // 폰 번호로 유저 조회하여 데이터가 존재하는지를 Bool 형태로 체크
        Boolean isExist = userRepository.existsByEmail(request.getEmail());
        // 리스폰스 생성후 반환
        CheckEmailResponse response = new CheckEmailResponse(isExist);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 사용자 정보 조회
    public ResponseEntity<?> getUserInfo(int userCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }
        // 리스폰스 생성
        UserInfoResponse response = UserInfoResponse.builder()
                .userCode(user.getUserCode())
                .name(user.getName())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 비밀번호 수정
    public ResponseEntity<?> modifyPassword(int userCode, ModifyPasswordRequest request) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 요청받은 비밀번호를 해쉬화
        String newHashedPassword = passwordEncoder.encode(request.getPassword());
        // 수정
        user.setPassword(newHashedPassword);
        // 저장
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body("비밀번호 수정 성공");
    }


    // 농장주 계좌번호 변경
    public ResponseEntity<?> updateBCAddress(int userCode, String address) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 유효한 지갑 주소인지 검사
        if(!WalletUtils.isValidAddress(address)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않는 지갑 주소입니다.");
        }

        // 수정후 저장
        user.setBcAddress(address);
        userRepository.save(user);
        
        // 입력받은 지갑 주소를 whitelist 에 추가
        blockChainService.InsertWhiteList(address);

        return ResponseEntity.ok().body("지갑 주소 업데이트 완료.");
    }

    // 회원 탈퇴
    public ResponseEntity<String> deleteUser(int userCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // isDelete = true, deleteTime = 현재 시간 으로 수정 후 저장
        user.setIsDelete(true);
        user.setDeleteTime(LocalDateTime.now());
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body("탈퇴 성공");
    }


    // 금융망 API 를 통해 계좌를 생성하는 메소드
    public void financeAccountCreate(int userCode, String userKey) {

        // 현재 시간 불러와서 적절한 형태로 파싱
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
        headerMap.put("apiName", "openAccount");
        // 요청날짜
        headerMap.put("transmissionDate", now.format(dateFormatter));
        headerMap.put("transmissionTime", now.format(timeFormatter));
        // 기관코드 고정
        headerMap.put("institutionCode", "00100");
        //핀테크 앱 고정
        headerMap.put("fintechAppNo", "001");
        headerMap.put("apiServiceCode", "openAccount");
        // 기관 거래 고유 번호 : 새로운 번호로 임의 채번 (YYYYMMDD + HHMMSS + 일련번호 6자리) 또는 20자리의 난수
        headerMap.put("institutionTransactionUniqueNo", institutionTransactionUniqueNo);
        // 개발자 키
        headerMap.put("apiKey", "2afacf41e60a4482b5c4997d194a46f0");
        // 계정생성해야함 -> 회원가입시 이메일을 작성하면 생성됨
        headerMap.put("userKey", userKey);


        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("Header", headerMap);
        // 상품 고유번호
        bodyMap.put("accountTypeUniqueNo", "001-1-81fe2deafd1943"); // 한국은행 입출금 상품

        try {
            JsonNode jsonNode =  WebClient.create("https://finapi.p.ssafy.io")
                    .post()
                    .uri("/ssafy/api/v1/edu/account/openAccount")
                    .bodyValue(bodyMap) // 구성한 Map을 bodyValue에 전달
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            System.out.println("API 호출 결과: " + jsonNode);

            // 'REC' 필드에 접근
            JsonNode recNode = jsonNode.get("REC");
            if (recNode != null) { // 'REC' 필드가 존재하는지 확인
                // 'bankCode'와 'accountNo'에 접근
                String bankCode = recNode.get("bankCode").asText();
                String accountNo = recNode.get("accountNo").asText();
                Account account = new Account();
                account.setUserCode(userCode);
                account.setBankName(bankCode);
                account.setAccountNum(accountNo);
                account.setBalance(0);
                accountRepository.save(account);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


}}
