package com.secondproject.monthlycoffee.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class IncomeInfo {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ii_id", nullable = false) 
    private Long id;
    
    @Column(name = "ii_amount", nullable = false) 
    private Integer amount;
    
    @Column(name = "ii_note") 
    private String note;

	@Column(name = "ii_date")
    private LocalDate date;

    @JoinColumn(name = "ii_mi_id", nullable = false) 
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberInfo member;

    public IncomeInfo(Integer amount, String note, LocalDate date, MemberInfo member) {
        this.amount = amount;
        this.note = note;
        this.date = date;
        this.member = member;
    }
    
    public void modifyBudgetDetail(Integer amount, String note, LocalDate date) {
        if(amount != null) this.amount = amount;
        if(note != null) this.note = note;
        if(date != null) this.date = date;
    }

    public String convertDate(LocalDate date) {
        String dateConv = this.date.toString();
        return dateConv;
    }
}
