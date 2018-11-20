package com.github.ahmetcanik.validator.data.repository;

import com.github.ahmetcanik.validator.data.entity.HourlyStats;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HourlyStatsRepositoryTest {

	@Autowired
	private HourlyStatsRepository hourlyStatsRepository;

	@Test
	public void findByCustomerIdAndTime() {
		Timestamp time = Timestamp.valueOf("2018-11-19 22:00:00");

		HourlyStats saved = new HourlyStats();
		saved.setCustomerId(1);
		saved.setRequestCount(2);
		saved.setInvalidCount(1);
		saved.setTime(time);

		hourlyStatsRepository.save(saved);

		HourlyStats found = hourlyStatsRepository.findByCustomerIdAndTime(saved.getCustomerId(), time).get(0);
		assertEquals(saved, found);
	}

	@Test
	public void findByCustomerIdAndTimeGreaterThanEqualAndTimeLessThanOrderByTimeAsc() {
		int customerId = 2;

		// first request of the day
		Timestamp firstHour = Timestamp.valueOf("2018-11-19 00:00:00");
		HourlyStats first = new HourlyStats();
		first.setCustomerId(customerId);
		first.setRequestCount(3);
		first.setInvalidCount(1);
		first.setTime(firstHour);
		hourlyStatsRepository.save(first);

		// last request of the day
		Timestamp lastHour = Timestamp.valueOf("2018-11-19 23:59:59");
		HourlyStats second = new HourlyStats();
		second.setCustomerId(customerId);
		second.setRequestCount(4);
		second.setInvalidCount(2);
		second.setTime(lastHour);

		hourlyStatsRepository.save(second);

		// this one should be excluded since it belongs to next day
		Timestamp nextDay = Timestamp.valueOf("2018-11-20 00:00:00");
		HourlyStats excluded = new HourlyStats();
		excluded.setCustomerId(customerId);
		excluded.setRequestCount(5);
		excluded.setInvalidCount(2);
		excluded.setTime(nextDay);

		hourlyStatsRepository.save(excluded);

		Timestamp start = Timestamp.valueOf("2018-11-19 00:00:00");
		Timestamp end = Timestamp.valueOf("2018-11-20 00:00:00");

		List<HourlyStats> found =
				hourlyStatsRepository.findByCustomerIdAndTimeGreaterThanEqualAndTimeLessThanOrderByTimeAsc(customerId, start, end);
		List<HourlyStats> saved = Arrays.asList(first, second);
		assertEquals(saved, found);
	}
}