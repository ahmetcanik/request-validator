package com.github.ahmetcanik.validator.services;

import com.github.ahmetcanik.validator.CollectorRequest;

public interface RequestProcessorService {
	String processRequest(CollectorRequest collectorRequest);
}
