package com.secondproject.monthlycoffee.entity;

import java.time.LocalDate;

import com.secondproject.monthlycoffee.entity.shared.BaseTime;
import com.secondproject.monthlycoffee.entity.type.Gender;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberInfo extends BaseTime {

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
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public MemberInfo(String uid, String nickname, LocalDate birth, Gender gender) {
        this.uid = uid;
        this.nickname = nickname;
        this.birth = birth;
        this.gender = gender;
    }

    public void modifyNickname(String nickname) {
        this.nickname = nickname;
    }
}
