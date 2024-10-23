package com.ga.basic_auth.exception;

import com.ga.basic_auth.dto.response.ResponseTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(InformationNotFoundException.class)
    protected ResponseEntity<Object> handleInformationNotFoundException(InformationNotFoundException ex, WebRequest request) {
        return new ResponseTemplate(HttpStatus.NOT_FOUND,ex.getMessage());
    }

    @ExceptionHandler(InformationExistsException.class)
    protected ResponseEntity<Object> handleInformationExistsException(InformationExistsException ex, WebRequest request) {
        return new ResponseTemplate(HttpStatus.CONFLICT,ex.getMessage());
    }

    @ExceptionHandler(InvalidDataException.class)
    protected ResponseEntity<Object> handleInformationExistsException(InvalidDataException ex, WebRequest request) {
        return new ResponseTemplate(HttpStatus.BAD_REQUEST,ex.getMessage());
    }

    // Handle other exceptions here
}
