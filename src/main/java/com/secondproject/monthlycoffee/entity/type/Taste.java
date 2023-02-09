package com.secondproject.monthlycoffee.entity.type;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

@Getter
public enum Taste {
    SWEET("sweet", "단맛"), SOUR("sour", "신맛"), SAVORY("savory", "고소한맛"), BIITER("bitter", "쓴맛"), ETC("etc", "기타");

    private final String code;
    private final String title;

    private Taste(String code, String title) {
        this.code = code;
        this.title = title;
    }

    private static final Map<String, Taste> BY_CODE = new ConcurrentHashMap<>();

    static {
        Arrays.stream(values()).forEach(v -> BY_CODE.put(v.code, v));
    }

    public static Taste valueOfCode(String code) {
        return BY_CODE.get(code);
    }
}
