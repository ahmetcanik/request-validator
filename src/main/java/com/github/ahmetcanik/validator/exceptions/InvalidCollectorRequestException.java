package com.github.ahmetcanik.validator.exceptions;

public class InvalidCollectorRequestException extends Exception {
	public InvalidCollectorRequestException() {
		super();
	}

	public InvalidCollectorRequestException(String message) {
		super(message);
	}

	public InvalidCollectorRequestException(String message, Throwable cause) {
		super(message, cause);
	}
}
