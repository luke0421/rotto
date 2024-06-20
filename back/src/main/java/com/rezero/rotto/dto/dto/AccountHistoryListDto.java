package com.rezero.rotto.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
public class AccountHistoryListDto {
    private String transferName;
    private int amount;
    private LocalDateTime accountTime;
    private int depositOrWithdrawal;

}
