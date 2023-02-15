package com.secondproject.monthlycoffee.dto.expense;

import java.time.LocalDate;

import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.entity.type.CoffeeBean;
import com.secondproject.monthlycoffee.entity.type.LikeHate;
import com.secondproject.monthlycoffee.entity.type.Mood;
import com.secondproject.monthlycoffee.entity.type.Taste;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDto {
    @Schema(description = "커피 종류", example = "아메리카노") private String category;
    @Schema(description = "브랜드", example = "스타벅스") private String brand;
    @Schema(description = "지출 가격", example = "4500") private Integer price;
    @Schema(description = "메모 내용", example = "맛있다") private String memo;
    @Schema(description = "텀블러 사용 유무 false:무, true:유", example = "false") private Boolean tumbler;
    @Schema(description = "커피 맛(SWEET, SOUR, SAVORY, BITTER, ETC)", example = "SWEET") private Taste taste;
    @Schema(description = "카페 분위기(WORK, TALK, SELFIE)", example = "WORK") private Mood mood;
    @Schema(description = "커피 원두(BRAZIL, GUATEMALA, COLOMBIA, MEXICO, INDONESIA, VIETNAM, ETC)", example = "BRAZIL") private CoffeeBean bean;
    @Schema(description = "좋아요/싫어요/무난해요(LIKE, HATE, SOSO)", example = "LIKE") private LikeHate likeHate;
    @Schema(description = "결제 방법(0:현금/1:카드)", example = "0") private Integer payment;
    @Schema(description = "지출 날짜", example = "2023-02-15") private LocalDate date;
}
