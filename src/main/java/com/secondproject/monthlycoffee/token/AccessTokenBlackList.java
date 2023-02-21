package com.secondproject.monthlycoffee.token;

import java.util.concurrent.TimeUnit;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@RedisHash("accessToken")
public class AccessTokenBlackList {
    
    @Id
    private String token;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expiration;
}
