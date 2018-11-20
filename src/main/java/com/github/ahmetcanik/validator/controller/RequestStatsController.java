package com.github.ahmetcanik.validator.controller;

import com.github.ahmetcanik.validator.services.RequestStatsService;
import com.github.ahmetcanik.validator.services.entity.CustomerDailyStats;
import com.github.ahmetcanik.validator.services.entity.StatsQuery;
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
@RequestMapping("/stats")
public class RequestStatsController {

	private final RequestStatsService requestStatsService;

	@Autowired
	public RequestStatsController(RequestStatsService requestStatsService) {
		this.requestStatsService = requestStatsService;
	}

	@PostMapping()
	public ResponseEntity<CustomerDailyStats> getCustomerDailyStats(@RequestBody StatsQuery statsQuery) {
		return ResponseEntity.ok(requestStatsService.getCustomerDailyStats(statsQuery));
	}
}
