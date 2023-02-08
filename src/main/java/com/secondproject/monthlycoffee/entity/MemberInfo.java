package com.secondproject.monthlycoffee.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mi_id")
    private Long id;

    @Column(name = "mi_uid", nullable = false)
    private String uid;

    @Column(name = "mi_nickname", nullable = false)
    private String nickname;
    
    @Column(name = "mi_birth", nullable = false)
    private LocalDate birth;

    @Column(name = "mi_gender", nullable = false)
    private Integer gender;

}
