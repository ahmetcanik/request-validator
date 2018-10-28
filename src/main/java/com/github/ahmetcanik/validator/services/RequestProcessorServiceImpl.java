package com.github.ahmetcanik.validator.services;

import com.github.ahmetcanik.validator.CollectorRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class RequestProcessorServiceImpl implements RequestProcessorService {
	@Override
	public String processRequest(CollectorRequest collectorRequest) {
		// stub function
		// all valid request goes in here
		return "OK";
	}
}
