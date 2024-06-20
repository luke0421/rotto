package com.rezero.rotto.dto.request;

import com.rezero.rotto.dto.dto.HeaderDto;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CreateFinanceMoneyAccountRequest {
    private HeaderDto Header;
    private String accountTypeUniqueNo;

//    public CreateFinanceMoneyAccountRequest(
//            String apiName,
//            String transmissionDate,
//            String transmissionTime,
//            String institutionCode,
//            String fintechAppNo,
//            String apiServiceCode,
//            String institutionTransactionUniqueNo,
//            String apiKey,
//            String userKey,
//            String accountTypeUniqueNo) {
//        this.Header = Map.of(
//                "apiName", apiName,
//                "transmissionDate", transmissionDate,
//                "transmissionTime", transmissionTime,
//                "institutionCode", institutionCode,
//                "fintechAppNo", fintechAppNo,
//                "apiServiceCode", apiServiceCode,
//                "institutionTransactionUniqueNo", institutionTransactionUniqueNo,
//                "apiKey", apiKey,
//                "userKey", userKey
//        );
//        this.accountTypeUniqueNo = accountTypeUniqueNo;
//    }

    // Getter와 Setter 생략 (필요한 경우 추가)
}
