package com.github.ahmetcanik.validator.services;

import com.github.ahmetcanik.validator.CollectorRequest;
import com.github.ahmetcanik.validator.data.entity.Customer;
import com.github.ahmetcanik.validator.data.repository.CustomerRepository;
import com.github.ahmetcanik.validator.data.repository.IpBlacklistRepository;
import com.github.ahmetcanik.validator.exceptions.CustomerDisabledException;
import com.github.ahmetcanik.validator.exceptions.CustomerIdNotFoundException;
import com.github.ahmetcanik.validator.exceptions.InvalidCollectorRequestException;
import com.github.ahmetcanik.validator.exceptions.RemoteIpBlacklistedException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class RequestProcessorServiceImpl implements RequestProcessorService {
	private final CustomerRepository customerRepository;
	private final IpBlacklistRepository ipBlacklistRepository;

	@Autowired
	public RequestProcessorServiceImpl(CustomerRepository customerRepository, IpBlacklistRepository ipBlacklistRepository) {
		this.customerRepository = customerRepository;
		this.ipBlacklistRepository = ipBlacklistRepository;
	}

	@Override
	public void validateRequest(CollectorRequest collectorRequest) throws InvalidCollectorRequestException {
		// check if customer exists
		Optional<Customer> customer = customerRepository.findById(collectorRequest.getCustomerID());
		if (!customer.isPresent())
			throw new CustomerIdNotFoundException("Customer ID " + collectorRequest.getCustomerID() + " not found");

		// check if customer is enabled
		if (customer.get().getActive() != 1)
			throw new CustomerDisabledException("Customer with ID " + collectorRequest.getCustomerID() + " is disabled");

		// check if request IP is valid and not blacklisted
		int ip;
		try {
			ip = ByteBuffer.wrap(InetAddress.getByName(collectorRequest.getRemoteIP()).getAddress()).getInt();
		} catch (UnknownHostException e) {
			throw new RemoteIpBlacklistedException("Remote IP " + collectorRequest.getRemoteIP() + " is invalid", e);
		}
		if (ipBlacklistRepository.existsById(ip))
			throw new RemoteIpBlacklistedException("Remote IP " + collectorRequest.getRemoteIP() + "[" + ip + "] is blacklisted");

	}

	@Override
	public String processRequest(CollectorRequest collectorRequest) {
		// stub function
		// all valid request goes in here
		return "OK";
	}
}
