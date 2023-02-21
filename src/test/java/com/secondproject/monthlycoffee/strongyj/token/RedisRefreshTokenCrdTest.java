package com.secondproject.monthlycoffee.strongyj.token;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.test.context.ActiveProfiles;

import com.secondproject.monthlycoffee.token.RefreshToken;
import com.secondproject.monthlycoffee.token.RefreshTokenRepository;

@DataRedisTest
@ActiveProfiles("test")
public class RedisRefreshTokenCrdTest {

    @Autowired private RefreshTokenRepository refreshRepo;
    private final long REFRESH_EXPIRATION_TIME = 1000L;

    @Test
    void create() {
        refreshRepo.save(new RefreshToken(UUID.randomUUID().toString(), 1L, REFRESH_EXPIRATION_TIME));
    }

    @Test
    void read() {
        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), 1L, 
                REFRESH_EXPIRATION_TIME);
        refreshRepo.save(refreshToken);

        RefreshToken refreshToken2 = refreshRepo.findById(refreshToken.getToken()).get();

        assertThat(refreshToken.getToken()).isEqualTo(refreshToken2.getToken());
        assertThat(refreshToken.getMemberId()).isEqualTo(refreshToken2.getMemberId());
    }

    private final CountDownLatch waiter = new CountDownLatch(1);
    
    @Test
    void delete() throws InterruptedException {
        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), 1L, REFRESH_EXPIRATION_TIME);
        refreshRepo.save(refreshToken);
        
        waiter.await(REFRESH_EXPIRATION_TIME + 1l, TimeUnit.MILLISECONDS);
    
        assertThat(refreshRepo.findById(refreshToken.getToken()).isPresent()).isFalse();
    }
    
}
