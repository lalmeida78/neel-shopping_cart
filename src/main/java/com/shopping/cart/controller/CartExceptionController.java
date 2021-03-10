package com.shopping.cart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.shopping.cart.model.ExceptionResponse;
import com.shopping.cart.service.CartServiceException;

@ControllerAdvice
public class CartExceptionController {

	
	@ExceptionHandler(value = CartServiceException.class)
	public ResponseEntity<Object> exception(CartServiceException exception, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getErrorCode().value(), exception.getMessage());
		return new ResponseEntity<Object>(exceptionResponse, exception.getErrorCode());
	}
}
