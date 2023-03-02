package com.secondproject.monthlycoffee.doyouee;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import com.secondproject.monthlycoffee.entity.type.AuthDomain;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.secondproject.monthlycoffee.doyouee.dto.YearMonthTestDto;
import com.secondproject.monthlycoffee.entity.IncomeInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.type.Gender;
import com.secondproject.monthlycoffee.repository.IncomeInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@Slf4j
public class YearMonthTest {
    
    @Autowired private MemberInfoRepository memberRepo;
    @Autowired private IncomeInfoRepository incomeRepo;
    private Long memberId;
    @Autowired private EntityManager em;

    @DisplayName("더미데이터만들기")
    @BeforeAll
    void makeDummyData() {
        MemberInfo member = new MemberInfo(AuthDomain.KAKAO, "test", "testnick", LocalDate.of(1990, 1, 1), Gender.FEMALE);
        memberRepo.save(member);
        memberId = member.getId();

        List<IncomeInfo> incomeList = new ArrayList<>();
        // 202201 -> sum 4500
        incomeList.add(new IncomeInfo(1000, null, LocalDate.of(2022, 1, 1), member));
        incomeList.add(new IncomeInfo(2000, null, LocalDate.of(2022, 1, 5), member));
        incomeList.add(new IncomeInfo(1500, null, LocalDate.of(2022, 1, 31), member));
        // 202203 -> sum 1000
        incomeList.add(new IncomeInfo(1000, null, LocalDate.of(2022, 3, 15), member));
        // 202112 -> sum 1500
        incomeList.add(new IncomeInfo(1000, null, LocalDate.of(2021, 12, 1), member));
        incomeList.add(new IncomeInfo(500, null, LocalDate.of(2021, 12, 31), member));
        incomeRepo.saveAll(incomeList);
    }
    
    @BeforeEach
    void clear() {
        em.clear();
    }
    
    @Test
    void yearMonth() {
        log.info("year Month = {}", YearMonth.now());
    }

    @DisplayName("연월별로수입합통계")
    @Test
    void incomeSumByYearMonth() {
        List<IncomeInfo> resultList = em.createQuery("select i from IncomeInfo i order by i.date", IncomeInfo.class).getResultList();

        YearMonth standard = YearMonth.from(resultList.get(0).getDate());
        List<YearMonthTestDto> dtoList = new ArrayList<>();
        dtoList.add(new YearMonthTestDto(standard, 0));
        
        for(var value : resultList) {
            if(standard.equals(YearMonth.from(value.getDate()))) {
                dtoList.get(dtoList.size() - 1).sumCalc(value.getAmount());
            } else {
                standard = YearMonth.from(value.getDate());
                dtoList.add(new YearMonthTestDto(YearMonth.from(standard), value.getAmount()));
            }
        }

        Assertions.assertThat(dtoList.size()).isEqualTo(3);
        Assertions.assertThat(dtoList.get(0)).isEqualTo(new YearMonthTestDto(YearMonth.of(2021, 12), 1500));
        Assertions.assertThat(dtoList.get(1)).isEqualTo(new YearMonthTestDto(YearMonth.of(2022, 1), 4500));
        Assertions.assertThat(dtoList.get(2)).isEqualTo(new YearMonthTestDto(YearMonth.of(2022, 3), 1000));
    }

}
