package com.github.ahmetcanik.validator.data.repository;

import com.github.ahmetcanik.validator.data.entity.HourlyStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HourlyStatsRepository extends JpaRepository<HourlyStats, Integer> {
}
