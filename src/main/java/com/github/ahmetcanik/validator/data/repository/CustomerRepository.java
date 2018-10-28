package com.github.ahmetcanik.validator.data.repository;

import com.github.ahmetcanik.validator.data.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
