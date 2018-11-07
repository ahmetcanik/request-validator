package com.github.ahmetcanik.validator.controller;

import com.github.ahmetcanik.validator.CollectorRequest;
import com.github.ahmetcanik.validator.exceptions.InvalidCollectorRequestException;
import com.github.ahmetcanik.validator.services.RequestProcessorService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/processor")
public class RequestProcessorController {

	private final RequestProcessorService processorService;

	@Autowired
	public RequestProcessorController(RequestProcessorService processorService) {
		this.processorService = processorService;
	}

	@PostMapping()
	public ResponseEntity<String> processRequest(@RequestBody CollectorRequest collectorRequest) {
		// first validate request
		try {
			processorService.validateRequest(collectorRequest);
		} catch (InvalidCollectorRequestException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok(processorService.processRequest(collectorRequest));
	}
}
