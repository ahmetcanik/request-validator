package com.github.ahmetcanik.validator.data.repository;

import com.github.ahmetcanik.validator.data.entity.HourlyStats;
import com.github.ahmetcanik.validator.data.entity.LatestStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface HourlyStatsRepository extends JpaRepository<HourlyStats, Integer> {
	@Query("SELECT new  com.github.ahmetcanik.validator.data.entity.LatestStats(max(requestCount), max(invalidCount)) FROM HourlyStats WHERE " +
			"customerId = ?1 AND time >= ?2")
	List<LatestStats> findHourlyStatsByCustomerId(Integer customerId, Timestamp time);

	List<HourlyStats> findByCustomerIdAndTime(Integer customerId, Timestamp time);
}
