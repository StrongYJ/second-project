package com.secondproject.monthlycoffee.entity.type;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

@Getter
public enum CoffeeBean {
    // BRAZIL / GUATEMALA / COLOMBIA / MEXICO / INDONESIA / VIETNAM / ETC
    BRAZIL("brazil", "브라질"), GUATEMALA("guatemala", "과테말라"), COLOMBIA("colombia", "콜롬비아"), 
    MEXICO("mexico", "멕시코"), INDONESIA("indonesia", "인도네시아"), VIETNAM("vietnam", "베트남"),
    ETC("etc", "기타");

    private final String code;
    private final String title;

    private CoffeeBean(String code, String title) {
        this.code = code;
        this.title = title;
    }
    
    private static final Map<String, CoffeeBean> BY_CODE = new ConcurrentHashMap<>();

    static {
        Arrays.stream(values()).forEach(v -> BY_CODE.put(v.code, v));
    }

    public static CoffeeBean valueOfCode(String code) {
        return BY_CODE.get(code);
    }
}
