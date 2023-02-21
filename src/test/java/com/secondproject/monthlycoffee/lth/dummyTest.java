package com.secondproject.monthlycoffee.lth;

import com.secondproject.monthlycoffee.dto.expense.MessageExpenseDto;
import com.secondproject.monthlycoffee.entity.ExpenseInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.type.*;
import com.secondproject.monthlycoffee.repository.ExpenseInfoRepository;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Slf4j
@ActiveProfiles("test")
public class dummyTest {
    @Autowired MemberInfoRepository mRepo;
    @Autowired ExpenseInfoRepository eRepo;
    
    @Test
    @Transactional
    void dummyData() {
        MemberInfo dummyMember = new MemberInfo(AuthDomain.KAKAO, "test123", "test", LocalDate.now(), Gender.MALE);
        String[] category = {"아메리카노", "카페라떼", "바닐라라떼", "카라멜 마끼아또", "카푸치노"};
        String[] brand = {"스타벅스", "투썸플레이스", "빽다방", "이디야커피", "할리스커피"};
        Boolean[] tumbler = {true, false};
        Taste[] taste = {Taste.valueOf("SWEET"), Taste.valueOf("SOUR"), Taste.valueOf("SAVORY"), Taste.valueOf("BITTER")};
        Mood[] mood = {Mood.valueOf("WORK"), Mood.valueOf("TALK"), Mood.valueOf("SELFIE")};
        CoffeeBean[] bean = {CoffeeBean.valueOf("BRAZIL"), CoffeeBean.valueOf("GUATEMALA"), CoffeeBean.valueOf("COLOMBIA"), CoffeeBean.valueOf("MEXICO"), CoffeeBean.valueOf("INDONESIA")};
        LikeHate[] hate = {LikeHate.valueOf("LIKE"), LikeHate.valueOf("HATE"), LikeHate.valueOf("SOSO")};
        int dummySize = 1000;
        for(int i=0; i<dummySize; i++) {
            int random1 = (int) ((Math.random())*10%2);
            int random2 = (int) ((Math.random())*10%3);
            int random3 = (int) ((Math.random())*10%4);
            int random4 = (int) ((Math.random())*10%5);
            int randomM = (int) ((Math.random())*100%12)+1;
            int randomY = 0;
            Integer price = (int) (Math.round((Math.random()*10000)/100)*100);
            try {
                Thread.sleep(0);
                if (randomM == 2) {
                    randomY = (int) ((Math.random()) * 100 % 28)+1;
                } else if (randomM == 4 || randomM == 6 || randomM == 9 || randomM == 11) {
                    randomY = (int) ((Math.random()) * 100 % 30)+1;
                } else {
                    randomY = (int) ((Math.random()) * 100 % 31)+1;
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            LocalDate date = LocalDate.of(2023, randomM, randomY);


            ExpenseInfo entity1 = new ExpenseInfo(
                    category[random4],
                    brand[random4],
                    price,
                    "메모",
                    tumbler[random1],
                    taste[random3],
                    mood[random2],
                    bean[random4],
                    hate[random2],
                    random1,
                    date,
                    dummyMember);
            eRepo.save(entity1);
        }
    }
}
