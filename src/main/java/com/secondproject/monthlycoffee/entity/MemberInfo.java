package com.secondproject.monthlycoffee.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import com.secondproject.monthlycoffee.entity.shared.BaseTime;
import com.secondproject.monthlycoffee.entity.type.AuthDomain;
import com.secondproject.monthlycoffee.entity.type.Gender;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberInfo extends BaseTime implements Serializable {
//    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mi_id")
    private Long id;

    @Column(name = "mi_auth_domain", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthDomain authDomain;

    @Column(name = "mi_uid", nullable = false)
    private String uid;

    @Column(name = "mi_nickname", nullable = false)
    private String nickname;
    
    @Column(name = "mi_birth")
    private LocalDate birth;

    @Column(name = "mi_gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Builder
    public MemberInfo(AuthDomain authDomain, String uid, String nickname, LocalDate birth, Gender gender) {
        this.authDomain = authDomain;
        this.uid = uid;
        this.nickname = nickname;
        this.birth = birth;
        this.gender = gender;
    }

    @PrePersist
    private void randomNickname() {
        if(!StringUtils.hasText(this.nickname)) {
            final String[] nickname = {"매니아", "맨이야", "Mania", "덕후", "짱짱", "사랑나라사랑", "러버", "Lover"};
            this.nickname = "커피" + nickname[ThreadLocalRandom.current().nextInt(nickname.length)];
        }
    }

    public void modifyOptionalInfo(String nickname, LocalDate birth, Gender gender) {
        if(StringUtils.hasText(nickname)) this.nickname = nickname;
        if(Objects.nonNull(birth)) this.birth = birth;
        if(Objects.nonNull(gender)) this.gender = gender;
    }

}
