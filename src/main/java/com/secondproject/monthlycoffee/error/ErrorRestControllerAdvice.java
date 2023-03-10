package com.secondproject.monthlycoffee.error;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.secondproject.monthlycoffee.config.security.JwtProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.NoSuchElementException;

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

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException e) {
        return new ErrorResponse(e.toString(), e.getMessage());
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleTokenExpiredException(TokenExpiredException e) {
        return new ErrorResponse("TokenExpiredException", e.getMessage());
    }

    @ExceptionHandler(JWTVerificationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleJWTVerificationException(JWTVerificationException e) {
        return new ErrorResponse("JWTVerificationException", JwtProperties.INVALID_TOKEN_MESSAGE);
    }
    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        return new ErrorResponse(e.toString(), e.getMessage());

    }
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRuntimeException(RuntimeException e) {
        return new ErrorResponse(e.toString(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        return new ErrorResponse(e.toString(), e.getMessage());
    }


}
