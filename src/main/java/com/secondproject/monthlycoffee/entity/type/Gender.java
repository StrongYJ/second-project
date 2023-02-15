package com.secondproject.monthlycoffee.entity.type;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

@Getter
public enum Gender {
    NONE("none", "선택안함"), MALE("male", "남성"), FEMALE("female", "여성");
    
    @JsonValue
    private final String code;
    private final String title;

    private Gender(String code, String title) {
        this.code = code;
        this.title = title;
    }

    private static final Map<String, Gender> BY_CODE = new ConcurrentHashMap<>();

    static {
        Arrays.stream(values()).forEach(v -> BY_CODE.put(v.code, v));
    }

    public static Gender valueOfCode(String code) {
        return BY_CODE.get(code);
    }
}
