package com.secondproject.monthlycoffee.token;

import com.secondproject.monthlycoffee.config.security.JwtProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenAPIController {

    private final TokenService tokenService;

    @PostMapping(JwtProperties.REISSUE_TOKEN_URI)
    public ResponseEntity<TokenResponseDto> postReissueAccessToken(HttpServletRequest request) {

        return new ResponseEntity<>(new TokenResponseDto("액세스 토큰이 재발급되었습니다", true), )
    }
}
