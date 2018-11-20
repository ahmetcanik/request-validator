package com.github.ahmetcanik.validator.services;

import com.github.ahmetcanik.validator.services.entity.CustomerDailyStats;
import com.github.ahmetcanik.validator.services.entity.StatsQuery;

public interface RequestStatsService {
	CustomerDailyStats getCustomerDailyStats(StatsQuery statsQuery);
}
