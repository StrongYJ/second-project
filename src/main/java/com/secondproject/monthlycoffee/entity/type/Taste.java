package com.secondproject.monthlycoffee.entity.type;

import lombok.Getter;

@Getter
public enum Taste {
    SWEET("단맛"), SOUR("신맛"), SAVORY("고소한맛"), BITTER("쓴맛"), ETC("기타");

    private final String title;

    private Taste(String title) {
        this.title = title;
    }
}
