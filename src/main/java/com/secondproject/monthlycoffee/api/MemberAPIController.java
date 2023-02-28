package com.secondproject.monthlycoffee.api;

import com.secondproject.monthlycoffee.config.security.AuthMember;
import com.secondproject.monthlycoffee.config.security.JwtProperties;
import com.secondproject.monthlycoffee.config.security.JwtUtil;
import com.secondproject.monthlycoffee.config.security.dto.AuthDto;
import com.secondproject.monthlycoffee.dto.member.*;
import com.secondproject.monthlycoffee.token.TokenDto;
import com.secondproject.monthlycoffee.token.TokenResponseDto;
import com.secondproject.monthlycoffee.token.TokenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
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

import com.secondproject.monthlycoffee.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "회원 API", description = "회원 정보 CRUD API")
public class MemberAPIController {
    private final MemberService memberService;
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;


    // 회원 등록
    @SecurityRequirements
    @Operation(
            summary = "회원 로그인",
            description = "미가입 회원은 자동으로 회원가입됩니다.<br>" +
                    "액세스토큰 헤더이름: " + HttpHeaders.AUTHORIZATION + 
                    " 유효 시간: " + (JwtProperties.ACCESS_EXPIRATION_TIME / (1000 * 60)) + "분<br>" +
                    " 리프레시토큰 헤더이름: " + JwtProperties.REFRESH_HEADER_NAME + 
                    " 유효 시간: " + (JwtProperties.REFRESH_EXPIRATION_TIME / (1000 * 60 * 60)) + "시간"
    )
    @PostMapping("")
    public ResponseEntity<LoginResponseDto> postMember(
        @Parameter(description = "로그인(등록)할 회원 정보") @RequestBody MemberLoginDto data
        ) {
        MemberDto memberDto = memberService.login(data);
        TokenDto tokenDto = tokenService.login(memberDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, tokenDto.access());
        headers.add(JwtProperties.REFRESH_HEADER_NAME, tokenDto.refresh());

        return new ResponseEntity<>(new LoginResponseDto(memberDto), headers, HttpStatus.CREATED);
    }

    @Operation(summary = "회원 로그아웃", description = "토큰을 무효화합니다.")
    @PostMapping("/logout")
    public ResponseEntity<TokenResponseDto> postMember(@AuthMember AuthDto authDto, HttpServletRequest request) {
        tokenService.logout(authDto.id(), request.getHeader(HttpHeaders.AUTHORIZATION));
        return new ResponseEntity<>(new TokenResponseDto("로그아웃되었습니다.", true), HttpStatus.CREATED);
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
    @Operation(summary = "회원 상세 조회", description = "등록된 회원 정보들 중 특정 회원의 id를 받아 조회합니다.")
    @GetMapping("/{member-id}")
    public ResponseEntity<MemberDto> getMemberDetail(
            @Parameter(description = "회원 식별 번호") @PathVariable("member-id") Long id,
            @AuthMember AuthDto authDto
    ) {
        checkVaildMemberId(id, authDto, "본인 정보만 조회가능합니다.");
        return new ResponseEntity<>(memberService.memberDetail(id), HttpStatus.OK);
    }



    // 회원 수정
    @Operation(summary = "회원 수정", description = "등록된 회원 정보들 중 특정 회원을 수정합니다.")
    @PatchMapping("/{member-id}")
    public ResponseEntity<MemberDto> patchMember(
        @Parameter(description = "회원 수정 내용") @RequestBody MemberEditDto edit,
        @Parameter(description = "회원 식별 번호", example = "1") @PathVariable("member-id") Long memberId,
        @AuthMember AuthDto authDto
        ) {
        checkVaildMemberId(memberId, authDto, "본인 계정만 수정가능합니다.");
        return new ResponseEntity<>(memberService.modifyMember(edit, memberId), HttpStatus.OK);
    }


    // 회원 삭제
    @Operation(summary = "회원 삭제", description = "등록된 회원 정보들 중 특정 회원을 삭제합니다.")
    @DeleteMapping("/{member-id}")
    public ResponseEntity<MemberDeleteDto> deleteMember(
        @Parameter(description = "회원 식별 번호", example = "1") @PathVariable("member-id") Long memberId,
        @AuthMember AuthDto authDto, HttpServletRequest request
        ) {
        checkVaildMemberId(memberId, authDto, "본인 계정만 삭제가능합니다.");
        MemberDeleteDto memberDeleteDto = memberService.deleteMember(memberId);
        tokenService.logout(memberId, request.getHeader(HttpHeaders.AUTHORIZATION));
        return new ResponseEntity<>(memberDeleteDto, HttpStatus.OK);
    }
    private static void checkVaildMemberId(Long id, AuthDto authDto, String message) {
        if (id != authDto.id()) {
            throw new IllegalArgumentException(message);
        }
    }
}
