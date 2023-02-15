package com.secondproject.monthlycoffee.entity.type;

import lombok.Getter;

@Getter
public enum Mood {
    // work / talk / selfie / 기타
    WORK("공부"), TALK("수다"), SELFIE("사진");
    
    private final String title;

    private Mood(String title) {
        this.title = title;
    }
}
