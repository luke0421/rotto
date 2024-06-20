package com.rezero.rotto.api.service;

import com.rezero.rotto.dto.request.SubscriptionProduceRequest;
import org.springframework.http.ResponseEntity;


public interface SubscriptionService {

    ResponseEntity<?> getSubscriptionList(int userCode, Integer page, Integer subsStatus, Integer minPrice, Integer maxPrice, String beanType, String sort, String keyword);
    ResponseEntity<?> getSubscriptionDetail(int userCode, int subscriptionCode);


	ResponseEntity<?> calculateSubscription(int subscriptionCode);

    ResponseEntity<?> postSubscription(int userCode, SubscriptionProduceRequest subscriptionProduceRequest);

	ResponseEntity<?> refundSubscription(int userCode, int subscriptionCode);
}
