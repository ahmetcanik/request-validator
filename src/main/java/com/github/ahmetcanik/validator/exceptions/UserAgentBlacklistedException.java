package com.github.ahmetcanik.validator.exceptions;

public class UserAgentBlacklistedException extends InvalidCollectorRequestException {
	public UserAgentBlacklistedException() {
		super();
	}

	public UserAgentBlacklistedException(String message) {
		super(message);
	}

	public UserAgentBlacklistedException(String message, Throwable cause) {
		super(message, cause);
	}
}
