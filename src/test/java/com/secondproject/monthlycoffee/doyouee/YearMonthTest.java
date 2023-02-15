package com.secondproject.monthlycoffee.doyouee;

import java.time.YearMonth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class YearMonthTest {
    
    @Test
    void yearMonth() {
        log.info("year Month = {}", YearMonth.now());
    }


}
