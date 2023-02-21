package com.secondproject.monthlycoffee.config.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            doFilter(request, response, filterChain);
        } catch (TokenExpiredException e) {
            exceptionMessage(401, "The access token expired", response);
        } catch (JWTVerificationException e) {
            exceptionMessage(401, "Invalid Token", response);
        }
    }

    private void exceptionMessage(final int statusCode, final String message, HttpServletResponse response) throws IOException, JsonProcessingException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(statusCode);
        Map<String, Object> errorJson = new LinkedHashMap<>();
        errorJson.put("status", HttpStatus.valueOf(statusCode).toString());
        errorJson.put("message", message);
        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(errorJson));
        writer.flush();
        writer.close();
    }
    
}
