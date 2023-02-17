package com.secondproject.monthlycoffee.config.security;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.TokenExpiredException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.hasText(authorization) && authorization.startsWith(JwtProperties.ACCESS_TOKEN_PREFIX)) {
            String token = jwtUtil.resolve(authorization);

            if(request.getRequestURI().equals(JwtProperties.REISSUE_TOKEN_URI) && 
                StringUtils.hasText(request.getHeader(JwtProperties.REFRESH_HEADER_NAME))
            ) {
                try {
                    jwtUtil.verifyAccessAndExtractClaim(token);
                } catch (TokenExpiredException e) {
                    final Long memberId = JWT.decode(token).getClaim("memberId").asLong();
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(memberId,
                        null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
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
