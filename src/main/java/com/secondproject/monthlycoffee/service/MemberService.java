package com.secondproject.monthlycoffee.service;

import com.secondproject.monthlycoffee.repository.*;
import com.secondproject.monthlycoffee.token.RefreshTokenRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.secondproject.monthlycoffee.dto.member.MemberDeleteDto;
import com.secondproject.monthlycoffee.dto.member.MemberDto;
import com.secondproject.monthlycoffee.dto.member.MemberEditDto;
import com.secondproject.monthlycoffee.dto.member.MemberLoginDto;
import com.secondproject.monthlycoffee.entity.MemberInfo;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberInfoRepository memberRepo;
    private final BudgetInfoRepository budgetRepo;
    private final CommentInfoRepository commentRepo;
    private final ExpenseInfoRepository expenseRepo;
    private final IncomeInfoRepository incomeRepo;
    private final LovePostInfoRepository lovePostRepo;

    // 회원 등록
    public MemberDto login(MemberLoginDto data) {
        Optional<MemberInfo> member = memberRepo.findByAuthDomainAndUid(data.authDomain(), data.uid());
        MemberDto memberDto;
        if(member.isPresent()) {
            memberDto = new MemberDto(member.get());
        } else {
            MemberInfo newMember = data.toEntity();
            memberRepo.save(newMember);
            memberDto = new MemberDto(newMember);
        }
        return memberDto;
    }

    // 회원 전체 리스트 조회
    @Transactional(readOnly = true)
    public Page<MemberDto> memberList(Pageable pageable) {
        return memberRepo.findAll(pageable).map(MemberDto::new);
    }
    
    // 회원 상세 조회
    @Transactional(readOnly = true)
    public MemberDto memberDetail(final Long id) {
        MemberInfo member = memberRepo.findById(id).orElseThrow();
        return new MemberDto(member);
    }

    // 회원 수정
    public MemberDto modifyMember(MemberEditDto edit, Long id) {
        MemberInfo member = memberRepo.findById(id).orElseThrow();
        if(member.getId()!=id) {
            throw new IllegalArgumentException("본인만 수정이 가능합니다."); 
        }
        if(memberRepo.existsByNickname(edit.nickname())){
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다."); 
        }
        member.modifyNickname(edit.nickname());
        return new MemberDto(member);
    }
    
    // 회원 삭제
    public MemberDeleteDto deleteMember(Long id) {
        MemberInfo member = memberRepo.findById(id).orElseThrow();
        if(member.getId()!=id) {
            throw new IllegalArgumentException("본인만 삭제 가능합니다."); 
        }
        budgetRepo.deleteByMember(member);
        incomeRepo.deleteByMember(member);
        lovePostRepo.deleteByMember(member);
        commentRepo.deleteByMember(member);
        expenseRepo.updateMemberNullByMember(member);
        memberRepo.delete(member);
        return new MemberDeleteDto(id, "회원이 탈퇴되었습니다.");
    }
}
