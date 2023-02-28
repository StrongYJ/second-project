package com.secondproject.monthlycoffee.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondproject.monthlycoffee.token.AccessTokenBlackListRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AccessTokenBlackListRepository accessTokenBlackListRepository;

    public JwtFilter(JwtUtil jwtUtil, AccessTokenBlackListRepository accessTokenBlackListRepository) {
        this.jwtUtil = jwtUtil;
        this.accessTokenBlackListRepository = accessTokenBlackListRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.hasText(authorization) && authorization.startsWith(JwtProperties.ACCESS_TOKEN_PREFIX)) {
            String token = jwtUtil.resolve(authorization);

            if(accessTokenBlackListRepository.existsById(token)) {
                response.setStatus(403);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                Map<String, Object> errorJson = new LinkedHashMap<>();
                errorJson.put("status", HttpStatus.valueOf(403).toString());
                errorJson.put("message", "This token is blacked");
                PrintWriter writer = response.getWriter();
                writer.write(new ObjectMapper().writeValueAsString(errorJson));
                writer.flush();
                writer.close();
                return;
            }

            if(request.getRequestURI().equals(JwtProperties.REISSUE_TOKEN_URI) && 
                StringUtils.hasText(request.getHeader(JwtProperties.REFRESH_HEADER_NAME))
            ) {
                log.info("[JwtFilter] 토큰 재발급 API 호출");
                try {
                    final Long memberId = jwtUtil.verifyAccessAndExtractClaim(token);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(memberId,
                            null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("[JwtFilter] 만료되지 않은 액세스 토큰 재발급");
                } catch (TokenExpiredException e) {
                    final Long memberId = JWT.decode(token).getClaim("memberId").asLong();
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(memberId,
                        null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("[JwtFilter] 만료된 액세스 토큰 재발급");
                }
            } else {
                final Long memberId = jwtUtil.verifyAccessAndExtractClaim(token);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(memberId, 
                    null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            
        }
        doFilter(request, response, filterChain);
    }
}
