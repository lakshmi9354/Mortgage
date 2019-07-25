package com.hcl.mortgage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcl.mortgage.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

	Customer findByLoginIdAndPassword(String loginId, String password);

}
