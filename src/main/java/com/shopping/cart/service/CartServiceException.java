package com.shopping.cart.service;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CartServiceException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HttpStatus errorCode;

	public CartServiceException() {
		super();
	}
	
	public CartServiceException(String message, HttpStatus errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	
	public CartServiceException(String message, HttpStatus errorCode, Throwable e) {
		super(message, e);
		this.errorCode = errorCode;
	}

}
