package com.github.ahmetcanik.validator.services.entity;

import java.util.Map;

public class CustomerDailyStats {
	private final Map<Integer, CustomerHourlyStats> hourlyStats;
	private long totalRequestCount;

	public CustomerDailyStats(Map<Integer, CustomerHourlyStats> hourlyStats) {
		this.hourlyStats = hourlyStats;
		totalRequestCount = this.hourlyStats.values().stream().mapToLong(CustomerHourlyStats::getRequestCount).sum();
	}

	public Map<Integer, CustomerHourlyStats> getHourlyStats() {
		return hourlyStats;
	}

	public long getTotalRequestCount() {
		return totalRequestCount;
	}
}
