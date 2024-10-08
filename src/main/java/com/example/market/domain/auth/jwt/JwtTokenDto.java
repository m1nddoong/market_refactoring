package com.example.market.domain.auth.jwt;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class JwtTokenDto {
    private Long userId;
    private String accessToken;
    private LocalDateTime expiredDate; // 토큰 만료 날짜
    private Long expiredSecond; // 토큰 만료 시간 (초단위)
}
