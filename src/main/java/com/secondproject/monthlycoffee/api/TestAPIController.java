package com.secondproject.monthlycoffee.api;

import com.secondproject.monthlycoffee.config.security.dto.AuthMemberDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secondproject.monthlycoffee.config.security.AuthMember;

@RestController
public class TestAPIController {

    @PostMapping("/test")
    public String test(@AuthMember AuthMemberDto authMember){
        return "test" + authMember.id();
    }
}
