package com.github.ahmetcanik.validator.exceptions;

public class CustomerIdNotFoundException extends InvalidCollectorRequestException {
	public CustomerIdNotFoundException() {
		super();
	}

	public CustomerIdNotFoundException(String message) {
		super(message);
	}

	public CustomerIdNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
