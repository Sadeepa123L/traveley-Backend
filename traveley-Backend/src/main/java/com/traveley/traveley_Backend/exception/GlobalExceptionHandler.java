package com.traveley.traveley_Backend.exception;


import com.traveley.traveley_Backend.dto.APIResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public APIResponseDTO handleUserNameNotFoundException(Exception ex){
        return new APIResponseDTO(
            HttpStatus.NOT_FOUND.value(),
            "Username not found",
                ex.getMessage()
        );
    }
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public APIResponseDTO handleBadCredentialException(BadCredentialsException ex) {
        return new APIResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "Username or Password incorrect",
                ex.getMessage());
    }
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public APIResponseDTO handleExpiredJwtException(ExpiredJwtException ex) {
        return new APIResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "expired token",
                ex.getMessage());
    }
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIResponseDTO handleRuntimeException(RuntimeException ex) {
        ex.printStackTrace();
        return new APIResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "error occurred",
                ex.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponseDTO handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new APIResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                errorMessage
        );
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public APIResponseDTO handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return new APIResponseDTO(
                HttpStatus.CONFLICT.value(),
                "Database Constraint Violation",
                "Data already exists or database constraint violated."
        );
    }
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public APIResponseDTO handleAccessDeniedException(AccessDeniedException ex) {
        return new APIResponseDTO(
                HttpStatus.FORBIDDEN.value(),
                "Access Denied",
                "You do not have permission to access this resource."
        );
    }
}
