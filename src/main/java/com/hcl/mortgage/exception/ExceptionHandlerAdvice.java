package com.hcl.mortgage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.hcl.mortgage.entity.ErrorReponse;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	private ResponseEntity<ErrorReponse> handleAllExceptions(Exception ex) {
		ErrorReponse errorResponse = new ErrorReponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MortgageException.class)
	private ResponseEntity<ErrorReponse> handleExceptions(MortgageException ex) {
		ErrorReponse errorResponse = new ErrorReponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}
	
}