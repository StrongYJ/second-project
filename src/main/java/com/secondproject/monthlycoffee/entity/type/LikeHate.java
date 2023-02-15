package com.secondproject.monthlycoffee.entity.type;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

@Getter
public enum LikeHate {
    LIKE("like", "좋아요"), SOSO("soso", "무난"), HATE("hate", "싫어요");
    
    @JsonValue
    private final String code;
    private final String title;

    private LikeHate(String code, String title) {
        this.code = code;
        this.title = title;
    }

    private static final Map<String, LikeHate> BY_CODE = new ConcurrentHashMap<>();

    static {
        Arrays.stream(values()).forEach(v -> BY_CODE.put(v.code, v));
    }

    public static LikeHate valueOfCode(String code) {
        return BY_CODE.get(code);
    }
}
