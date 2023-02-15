package com.secondproject.monthlycoffee.api;

import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secondproject.monthlycoffee.dto.member.MemberDeleteDto;
import com.secondproject.monthlycoffee.dto.member.MemberDto;
import com.secondproject.monthlycoffee.dto.member.MemberEditDto;
import com.secondproject.monthlycoffee.dto.member.MemberNewDto;
import com.secondproject.monthlycoffee.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Tag(name = "회원 관리", description = "회원 정보 CRUD API")
public class MemberAPIController {
    private final MemberService memberService;


    // 회원 등록
    @Operation(summary = "회원 등록", description = "회원을 등록합니다.")
    @PostMapping("")
    public ResponseEntity<MemberDto> postMember(
        @Parameter(description = "등록 할 회원 정보") @RequestBody MemberNewDto data
        ) {
            return new ResponseEntity<>(memberService.newMember(data), HttpStatus.CREATED);
    }


    // 회원 전체 리스트 조회
    @Operation(summary = "회원 전체 리스트 조회", description = "등록된 회원 정보들을 10개 단위로 보여줍니다.")
    @GetMapping("")
    @PageableAsQueryParam
    public ResponseEntity<Page<MemberDto>> getMemberList(
        @Parameter(hidden = true) @PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
            return new ResponseEntity<>(memberService.memberList(pageable), HttpStatus.OK);
    }


    // 회원 상세 조회
    @Operation(summary = "회원 상세 조회", description = "등록된 회원 정보들 중 특정 회원의 UID를 받아 조회합니다.")
    @GetMapping("/{member-uid}")
    public ResponseEntity<MemberDto> getMemberDetail(
        @Parameter(description = "회원 UID", example = "123@#!4SDFKC") @PathVariable("member-uid") String memberUid) {
            return new ResponseEntity<>(memberService.memberDetail(memberUid), HttpStatus.OK);
    }

    
    // 회원 수정
    @Operation(summary = "회원 수정", description = "등록된 회원 정보들 중 특정 회원을 수정합니다.")
    @PatchMapping("/{member-id}")
    public ResponseEntity<MemberDto> patchMember(
        @Parameter(description = "회원 수정 내용") MemberEditDto edit,
        @Parameter(description = "회원 식별 번호", example = "1") @PathVariable("member-id") Long memberId
        ) {
            return new ResponseEntity<>(memberService.modifyMember(edit, memberId), HttpStatus.OK);
    }


    // 회원 삭제
    @Operation(summary = "회원 삭제", description = "등록된 회원 정보들 중 특정 회원을 삭제합니다.")
    @DeleteMapping("/{member-id}")
    public ResponseEntity<MemberDeleteDto> deleteMember(
        @Parameter(description = "회원 식별 번호", example = "1") @PathVariable("member-id") Long memberId
        ) {
            return new ResponseEntity<>(memberService.deleteMember(memberId), HttpStatus.OK);
    }
    
}
