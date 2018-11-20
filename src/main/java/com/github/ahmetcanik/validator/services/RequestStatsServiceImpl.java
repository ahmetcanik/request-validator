package com.github.ahmetcanik.validator.services;

import com.github.ahmetcanik.validator.data.entity.HourlyStats;
import com.github.ahmetcanik.validator.data.repository.HourlyStatsRepository;
import com.github.ahmetcanik.validator.services.entity.CustomerDailyStats;
import com.github.ahmetcanik.validator.services.entity.CustomerHourlyStats;
import com.github.ahmetcanik.validator.services.entity.StatsQuery;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class RequestStatsServiceImpl implements RequestStatsService {

	private HourlyStatsRepository hourlyStatsRepository;

	@Autowired
	public RequestStatsServiceImpl(HourlyStatsRepository hourlyStatsRepository) {
		this.hourlyStatsRepository = hourlyStatsRepository;
	}

	@Override
	public CustomerDailyStats getCustomerDailyStats(StatsQuery statsQuery) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(statsQuery.getTimestamp());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Timestamp start = Timestamp.from(calendar.toInstant());
		// next day
		calendar.add(Calendar.DATE, 1);
		Timestamp end = Timestamp.from(calendar.toInstant());
		List<HourlyStats> hourlyStats =
				hourlyStatsRepository.findByCustomerIdAndTimeGreaterThanEqualAndTimeLessThanOrderByTimeAsc(statsQuery.getCustomerID(), start, end);
		return new CustomerDailyStats(
				hourlyStats.stream().collect(Collectors.toMap(hs -> {
					Calendar hourCalendar = Calendar.getInstance();
					hourCalendar.setTimeInMillis(hs.getTime().getTime());
					return hourCalendar.get(Calendar.HOUR_OF_DAY);
				}, hs -> new CustomerHourlyStats(hs.getRequestCount(), hs.getInvalidCount()), (v1, v2) -> v1, TreeMap::new)));
	}
}
