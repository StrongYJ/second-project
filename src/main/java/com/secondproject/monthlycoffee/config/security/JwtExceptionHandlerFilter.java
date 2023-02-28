package com.secondproject.monthlycoffee.config.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import com.secondproject.monthlycoffee.error.ErrorResponse;
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
            ErrorResponse error = new ErrorResponse("TokenExpiredException", e.getMessage());
            exceptionMessage(401, error, response);
        } catch (JWTVerificationException e) {
            ErrorResponse error = new ErrorResponse("JWTVerificationException", JwtProperties.INVALID_TOKEN_MESSAGE);
            exceptionMessage(401, error, response);
        }
    }

    private void exceptionMessage(final int statusCode, ErrorResponse errorResponse, HttpServletResponse response) throws IOException, JsonProcessingException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(statusCode);
        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(errorResponse));
        writer.flush();
        writer.close();
    }
    
}
