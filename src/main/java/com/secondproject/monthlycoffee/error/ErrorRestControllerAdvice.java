package com.secondproject.monthlycoffee.error;

import java.util.NoSuchElementException;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.secondproject.monthlycoffee.token.TokenResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.secondproject.monthlycoffee.api")
public class ErrorRestControllerAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSuchElementFoundException(NoSuchElementException e) {
        return new ErrorResponse(e.toString(), e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        return new ErrorResponse(e.toString(), e.getMessage());
    }

    @ExceptionHandler(JWTVerificationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public TokenResponseDto handleJWTVerificationException(JWTVerificationException e) {
        return new TokenResponseDto(e.toString(), e.getMessage(), false);
    }
}
