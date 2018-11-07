package com.github.ahmetcanik.validator.exceptions;

public class RemoteIpBlacklistedException extends InvalidCollectorRequestException {
	public RemoteIpBlacklistedException() {
		super();
	}

	public RemoteIpBlacklistedException(String message) {
		super(message);
	}

	public RemoteIpBlacklistedException(String message, Throwable cause) {
		super(message, cause);
	}
}
