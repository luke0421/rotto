// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

// 청약
struct Subscription {
    uint code;  // 청약 코드
    uint confirm_price; // 공모가
    uint32 limit_num; // 제한갯수 (1인당 최대 토큰 구매 개수)
}