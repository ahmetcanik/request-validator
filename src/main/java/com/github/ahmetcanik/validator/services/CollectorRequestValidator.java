package com.github.ahmetcanik.validator.services;

import com.github.ahmetcanik.validator.CollectorRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CollectorRequestValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		return CollectorRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "customerID", HttpStatus.BAD_REQUEST.toString(), "customerID.empty");

		CollectorRequest collectorRequest = (CollectorRequest) target;
	}
}
