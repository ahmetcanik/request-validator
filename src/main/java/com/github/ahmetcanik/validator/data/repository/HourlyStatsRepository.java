package com.github.ahmetcanik.validator.data.repository;

import com.github.ahmetcanik.validator.data.entity.HourlyStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface HourlyStatsRepository extends JpaRepository<HourlyStats, Integer> {
	List<HourlyStats> findByCustomerIdAndTime(Integer customerId, Timestamp time);

	List<HourlyStats> findByCustomerIdAndTimeGreaterThanEqualAndTimeLessThanOrderByTimeAsc(int customerId, Timestamp start, Timestamp end);
}
