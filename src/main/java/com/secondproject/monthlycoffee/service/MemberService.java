package com.secondproject.monthlycoffee.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.secondproject.monthlycoffee.dto.member.MemberDeleteDto;
import com.secondproject.monthlycoffee.dto.member.MemberDto;
import com.secondproject.monthlycoffee.dto.member.MemberEditDto;
import com.secondproject.monthlycoffee.dto.member.MemberNewDto;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.repository.MemberInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberInfoRepository memberRepo;

    // 회원 등록
    public MemberDto newMember(MemberNewDto data) {
        if(memberRepo.findByUid(data.uid()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }
        MemberInfo newMember = new MemberInfo(data.uid(), data.nickname(), data.birth(), data.gender());
        memberRepo.save(newMember);
        return new MemberDto(newMember);
    }

    // 회원 전체 리스트 조회
    @Transactional(readOnly = true)
    public Page<MemberDto> memberList(Pageable pageable) {
        return memberRepo.findAll(pageable).map(MemberDto::new);
    }
    
    // 회원 상세 조회
    @Transactional(readOnly = true)
    public MemberDto memberDetail(Long id) {
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
        memberRepo.delete(member);
        return new MemberDeleteDto(id, "회원이 탈퇴되었습니다.");
    }
}
