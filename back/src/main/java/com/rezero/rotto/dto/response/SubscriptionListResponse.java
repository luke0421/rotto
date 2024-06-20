package com.rezero.rotto.dto.response;

import com.rezero.rotto.dto.dto.SubscriptionListDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class SubscriptionListResponse {

    private List<SubscriptionListDto> subscriptions;
    private int totalPages;

}
