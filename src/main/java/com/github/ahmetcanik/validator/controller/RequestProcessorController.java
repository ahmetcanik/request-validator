package com.github.ahmetcanik.validator.controller;

import com.github.ahmetcanik.validator.CollectorRequest;
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

	@Autowired
	RequestProcessorService processorService;

	@PostMapping()
	public ResponseEntity<String> processRequest(@RequestBody CollectorRequest collectorRequest) {
		return ResponseEntity.ok(processorService.processRequest(collectorRequest));
	}
}
