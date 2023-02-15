package com.secondproject.monthlycoffee.entity.type;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

@Getter
public enum Gender {
    NONE("선택안함"), MALE("남성"), FEMALE("여성");
    
    private final String title;

    private Gender(String title) {
        this.title = title;
    }
}
