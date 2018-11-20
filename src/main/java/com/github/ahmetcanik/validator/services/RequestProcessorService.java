package com.github.ahmetcanik.validator.services;

import com.github.ahmetcanik.validator.services.entity.CollectorRequest;
import com.github.ahmetcanik.validator.exceptions.InvalidCollectorRequestException;

public interface RequestProcessorService {
	void validateRequest(CollectorRequest collectorRequest) throws InvalidCollectorRequestException;

	String processRequest(CollectorRequest collectorRequest);
}
