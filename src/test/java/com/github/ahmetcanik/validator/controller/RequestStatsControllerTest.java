package com.github.ahmetcanik.validator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ahmetcanik.validator.services.entity.CollectorRequest;
import com.github.ahmetcanik.validator.services.entity.CustomerDailyStats;
import com.github.ahmetcanik.validator.services.entity.CustomerHourlyStats;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.Map;
import java.util.TreeMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RequestStatsControllerTest {

	@Autowired
	private MockMvc mvc;

	@Test
	public void getCustomerDailyStats() throws Exception {
		// first make some requests
		// for each hour make 24-hour valid requests (no invalid requests)
		// for hour 00 : make 24
		// for hour 01 : make 23
		// ...
		// for hour 23 : make  1
		int customerID = 1;
		String day = "2018-11-19";
		Map<Integer, CustomerHourlyStats> expectedHourlyStats = new TreeMap<>();
		for (int hour = 0; hour < 24; hour++) {
			for (int requestCount = 24; requestCount > hour; requestCount--) {
				Timestamp hourTimestamp = Timestamp.valueOf(day + " " + hour + ":00:00");
				CollectorRequest collectorRequest = new CollectorRequest();
				collectorRequest.setCustomerID(customerID);
				collectorRequest.setTagID(requestCount);
				collectorRequest.setUserID("aaaaaaaa-bbbb-cccc-1111-222222222222");
				collectorRequest.setRemoteIP("123.234.56.78");
				collectorRequest.setTimestamp(hourTimestamp.getTime());

				String json = new ObjectMapper().writeValueAsString(collectorRequest);

				mvc.perform(post("/processor")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
						.andExpect(status().isOk())
						.andExpect(content().string("OK"));
			}
			expectedHourlyStats.put(hour, new CustomerHourlyStats(24 - hour, 0));
		}

		String queryJson = "{\"customerID\":" + customerID +
				", \"timestamp\":" + Timestamp.valueOf(day + " 00:00:00").getTime() + "}";

		CustomerDailyStats expectedDailyStats = new CustomerDailyStats(expectedHourlyStats);

		String expectedJson = new ObjectMapper().writeValueAsString(expectedDailyStats);

		mvc.perform(post("/stats")
				.contentType(MediaType.APPLICATION_JSON)
				.content(queryJson))
				.andExpect(status().isOk())
				.andExpect(content().json(expectedJson));
	}
}