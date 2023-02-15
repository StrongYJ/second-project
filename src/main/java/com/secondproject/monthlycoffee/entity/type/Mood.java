package com.secondproject.monthlycoffee.entity.type;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

@Getter
public enum Mood {
    // work / talk / selfie / 기타
    WORK("work", "공부"), TALK("talk", "수다"), SELFIE("selfie", "사진");

    @JsonValue
    private final String code;
    private final String title;

    private Mood(String code, String title) {
        this.code = code;
        this.title = title;
    }

    private static final Map<String, Mood> BY_CODE = new ConcurrentHashMap<>();

    static {
        Arrays.stream(values()).forEach(v -> BY_CODE.put(v.code, v));
    }

    public static Mood valueOfCode(String code) {
        return BY_CODE.get(code);
    }
}
