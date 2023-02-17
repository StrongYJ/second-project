package com.secondproject.monthlycoffee.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secondproject.monthlycoffee.config.security.LoginMemberId;

@RestController
public class TestAPIController {

    @PostMapping("/test")
    public String test(@LoginMemberId Long id){
        return "test" + id;
    }
}
