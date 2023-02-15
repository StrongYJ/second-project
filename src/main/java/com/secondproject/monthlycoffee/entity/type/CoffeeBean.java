package com.secondproject.monthlycoffee.entity.type;

import lombok.Getter;

@Getter
public enum CoffeeBean {
    // BRAZIL / GUATEMALA / COLOMBIA / MEXICO / INDONESIA / VIETNAM / ETC
    BRAZIL("브라질"), GUATEMALA("과테말라"), COLOMBIA("콜롬비아"), 
    MEXICO("멕시코"), INDONESIA("인도네시아"), VIETNAM("베트남"),
    ETC("기타");

    private final String title;

    private CoffeeBean(String title) {
        this.title = title;
    }
}
