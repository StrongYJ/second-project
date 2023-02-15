package com.secondproject.monthlycoffee.entity.type;

import lombok.Getter;

@Getter
public enum LikeHate {
    LIKE("좋아요"), SOSO("무난"), HATE("싫어요");
    

    private final String title;

    private LikeHate(String title) {
        this.title = title;
    }
}
