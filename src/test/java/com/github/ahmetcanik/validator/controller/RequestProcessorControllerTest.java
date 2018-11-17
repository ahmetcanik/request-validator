package com.github.ahmetcanik.validator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ahmetcanik.validator.CollectorRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.InetAddress;
import java.nio.ByteBuffer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RequestProcessorControllerTest {

	@Autowired
	private MockMvc mvc;

	@Test
	public void processRequest() throws Exception {
		CollectorRequest collectorRequest = new CollectorRequest();
		collectorRequest.setCustomerID(1);
		collectorRequest.setTagID(2);
		collectorRequest.setUserID("aaaaaaaa-bbbb-cccc-1111-222222222222");
		collectorRequest.setRemoteIP("123.234.56.78");
		collectorRequest.setTimestamp(1500000000);

		String json = new ObjectMapper().writeValueAsString(collectorRequest);

		mvc.perform(post("/processor")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isOk())
				.andExpect(content().string("OK"));
	}

	@Test
	public void processRequestInvalidContentType() throws Exception {
		mvc.perform(post("/processor")
				.contentType(MediaType.APPLICATION_XML)
				.content("{}")).andExpect(status().isUnsupportedMediaType());
	}

	@Test
	public void processRequestMalformedJSON() throws Exception {
		mvc.perform(post("/processor")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{")).andExpect(status().isBadRequest());
	}

	@Test
	public void processRequestMissingIntegerField() throws Exception {
		mvc.perform(post("/processor")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"tagID\":2,\"userID\":\"aaaaaaaa-bbbb-cccc-1111-222222222222\",\"remoteIP\":\"123.234.56.78\"," +
						"\"timestamp\":1500000000}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("customerID cannot be null"));
	}

	@Test
	public void processRequestMissingStringField() throws Exception {
		mvc.perform(post("/processor")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"customerId\":2,\"tagID\":2,\"remoteIP\":\"123.234.56.78\"," +
						"\"timestamp\":1500000000}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("userID cannot be null"));
	}

	@Test
	public void processRequestCustomerNotFound() throws Exception {
		CollectorRequest collectorRequest = new CollectorRequest();
		collectorRequest.setCustomerID(5);
		collectorRequest.setTagID(2);
		collectorRequest.setUserID("aaaaaaab-bbbb-cccc-1111-222222222222");
		collectorRequest.setRemoteIP("123.234.56.78");
		collectorRequest.setTimestamp(1500000000);

		String json = new ObjectMapper().writeValueAsString(collectorRequest);

		mvc.perform(post("/processor")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Customer ID " + collectorRequest.getCustomerID() + " not found"));
	}

	@Test
	public void processRequestCustomerDisabled() throws Exception {
		CollectorRequest collectorRequest = new CollectorRequest();
		collectorRequest.setCustomerID(3);
		collectorRequest.setTagID(2);
		collectorRequest.setUserID("aaaadaaac-bbbb-cccc-1111-222222222222");
		collectorRequest.setRemoteIP("123.234.56.78");
		collectorRequest.setTimestamp(1500000000);

		String json = new ObjectMapper().writeValueAsString(collectorRequest);

		mvc.perform(post("/processor")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Customer with ID " + collectorRequest.getCustomerID() + " is disabled"));
	}

	@Test
	public void processRequestIPBlacklisted() throws Exception {
		CollectorRequest collectorRequest = new CollectorRequest();
		collectorRequest.setCustomerID(1);
		collectorRequest.setTagID(2);
		collectorRequest.setUserID("aaaadaaad-bbbb-cccc-1111-222222222222");
		collectorRequest.setRemoteIP("127.0.0.1");
		collectorRequest.setTimestamp(1500000000);

		String json = new ObjectMapper().writeValueAsString(collectorRequest);

		int ip = ByteBuffer.wrap(InetAddress.getByName(collectorRequest.getRemoteIP()).getAddress()).getInt();

		mvc.perform(post("/processor")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Remote IP " + collectorRequest.getRemoteIP() + "[" + ip + "] is blacklisted"));
	}

	@Test
	public void processRequestUserAgentBlacklisted() throws Exception {
		CollectorRequest collectorRequest = new CollectorRequest();
		collectorRequest.setCustomerID(4);
		collectorRequest.setTagID(2);
		collectorRequest.setUserID("aaaadaaae-bbbb-cccc-1111-222222222222");
		collectorRequest.setRemoteIP("192.168.1.1");
		collectorRequest.setTimestamp(1500000000);

		String userAgent = "A6-Indexer";
		mvc.perform(post("/processor")
				.contentType(MediaType.APPLICATION_JSON)
				.header("User-Agent", userAgent)
				.content(new ObjectMapper().writeValueAsString(collectorRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("User-Agent " + userAgent + " is blacklisted"));
	}
}