package com.cognizant.pensionprocess.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 
 * @author Nayan, Akshita, Akhil
 * GlobalExceptionHandler class extends ResponseEntityExceptionHandler 
 * Will handle all type of exceptions
 */
@ControllerAdvice
public class ProcessGlobalExceptionHandlerClass extends ResponseEntityExceptionHandler{

	@ExceptionHandler(PensionerDetailNotFoundException.class)
    public ResponseEntity<Object> handleInvalidAadhaarException(
    		PensionerDetailNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Pensioner Detail Not Found");

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

	@ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleException(
    		RuntimeException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Pensioner Detail Not Found");

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}

