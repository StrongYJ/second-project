package com.secondproject.monthlycoffee.token;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@RedisHash("RefreshToken")
public class RefreshToken {
    
    @Id
    private String token;

    private Long memberId;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expiration;
}
