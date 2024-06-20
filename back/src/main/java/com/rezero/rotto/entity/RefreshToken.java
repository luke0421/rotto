package com.rezero.rotto.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@RedisHash(value = "refreshToken", timeToLive = 60*60*24*14)
public class RefreshToken {

    @Indexed
    private int userCode;

    @Id
    private String refreshToken;
}
