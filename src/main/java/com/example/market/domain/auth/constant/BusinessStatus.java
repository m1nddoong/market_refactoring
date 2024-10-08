package com.example.market.domain.auth.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessStatus {
    // 신청, 승인, 거절
    APPLIED, APPROVED, REJECTED
}
