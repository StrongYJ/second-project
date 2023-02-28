package com.secondproject.monthlycoffee.strongyj;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTest {
    @LocalServerPort
    private int post;

    @BeforeEach
    void apiTestInit() {
        RestAssured.port = post;
    }
}
