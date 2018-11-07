package com.github.ahmetcanik.validator.exceptions;

public class CustomerDisabledException extends InvalidCollectorRequestException {
	public CustomerDisabledException() {
		super();
	}

	public CustomerDisabledException(String message) {
		super(message);
	}

	public CustomerDisabledException(String message, Throwable cause) {
		super(message, cause);
	}
}
