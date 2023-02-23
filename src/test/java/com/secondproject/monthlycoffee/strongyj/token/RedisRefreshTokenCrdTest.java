package com.secondproject.monthlycoffee.strongyj.token;

import com.secondproject.monthlycoffee.token.RefreshToken;
import com.secondproject.monthlycoffee.token.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RedisRefreshTokenCrdTest {

    @Autowired private RefreshTokenRepository refreshRepo;
    private final long REFRESH_EXPIRATION_TIME = 1000L;
    private static final String initId = UUID.randomUUID().toString();

    @Test
    void create() {
        refreshRepo.save(new RefreshToken(initId, 1L, REFRESH_EXPIRATION_TIME));
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

    @Test
    void deleteByMemberId() {
        refreshRepo.save(new RefreshToken(initId, 1L, 100000000L));

        assertThat(refreshRepo.existsById(initId)).isTrue();

        refreshRepo.findAll().stream().filter(r -> Objects.nonNull(r) && r.getMemberId() == 1L ).forEach(refreshRepo::delete);

        assertThat(refreshRepo.existsById(initId)).isFalse();

    }
    
}
