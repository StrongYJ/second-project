package com.secondproject.monthlycoffee.strongyj.token;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.test.context.ActiveProfiles;

import com.secondproject.monthlycoffee.token.AccessTokenBlackList;
import com.secondproject.monthlycoffee.token.AccessTokenBlackListRepository;

@DataRedisTest
@ActiveProfiles("test")
public class RedisAccessTokenCrdTest {

    @Autowired private AccessTokenBlackListRepository access;

    @Test
    void create() {
        AccessTokenBlackList token = new AccessTokenBlackList("fwnfjwenfkwnfwknefkjnj", 1000L);
        access.save(token);


        access.deleteById(token.getToken());
    }

    @Test
    void read() {
        AccessTokenBlackList accessTokenBlackList = new AccessTokenBlackList(UUID.randomUUID().toString(), (1000l * 60 * 5));
        access.save(accessTokenBlackList);

        AccessTokenBlackList accessTokenBlackList2 = access.findById(accessTokenBlackList.getToken()).get();
        Assertions.assertThat(accessTokenBlackList.getToken()).isEqualTo(accessTokenBlackList2.getToken());

        access.deleteById(accessTokenBlackList.getToken());
    }

    @Test
    void delete() {
        AccessTokenBlackList accessTokenBlackList = new AccessTokenBlackList(UUID.randomUUID().toString(), (1000l * 60 * 5));
        access.save(accessTokenBlackList);

        access.deleteById(accessTokenBlackList.getToken());

        access.delete(accessTokenBlackList);
    }

    @Test
    void 만료() throws InterruptedException {
        AccessTokenBlackList accessTokenBlackList = new AccessTokenBlackList(UUID.randomUUID().toString(), 1000L);
        access.save(accessTokenBlackList);

        Thread.sleep(1000L);
        
        Assertions.assertThat(access.findById(accessTokenBlackList.getToken()).isPresent()).isEqualTo(false);

        access.delete(accessTokenBlackList);
    }
}
