package com.secondproject.monthlycoffee.dto.expense;

import com.secondproject.monthlycoffee.dto.post.ExpenseImageDto;
import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.entity.type.CoffeeBean;
import com.secondproject.monthlycoffee.entity.type.LikeHate;
import com.secondproject.monthlycoffee.entity.type.Mood;
import com.secondproject.monthlycoffee.entity.type.Taste;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseCreateDto {
    @Schema(description = "커피 종류", example = "아메리카노") private String category;
    @Schema(description = "브랜드", example = "스타벅스") private String brand;
    @Schema(description = "지출 가격", example = "4500") private Integer price;
    @Schema(description = "메모 내용", example = "맛있다", nullable = true) private String memo;
    @Schema(description = "텀블러 사용 유무 false:무, true:유", example = "false", nullable = true) private Boolean tumbler;
    @Schema(description = "커피 맛", example = "SWEET", nullable = true) private Taste taste;
    @Schema(description = "카페 분위기", example = "WORK", nullable = true) private Mood mood;
    @Schema(description = "커피 원두", example = "BRAZIL", nullable = true) private CoffeeBean bean;
    @Schema(description = "좋아요/싫어요/무난해요", example = "LIKE", nullable = true) private LikeHate likeHate;
    @Schema(description = "결제 방법(0:현금/1:카드)", example = "0", nullable = true) private Integer payment;
    @Schema(description = "지출 날짜", example = "2023-02-15") private LocalDate date;

    public ExpenseCreateDto(ExpenseInfo entity) {
        this.category = entity.getCategory();
        this.brand = entity.getBrand();
        this.price = entity.getPrice();
        this.memo = entity.getMemo();
        this.tumbler = entity.getTumbler();
        this.taste = entity.getTaste();
        this.mood = entity.getMood();
        this.bean = entity.getBean();
        this.likeHate = entity.getLikeHate();
        this.payment = entity.getPayment();
        this.date = entity.getDate();
    }
}
