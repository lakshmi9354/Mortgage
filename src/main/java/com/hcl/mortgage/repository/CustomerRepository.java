package com.hcl.mortgage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcl.mortgage.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

	Optional<Customer> findByLoginIdAndPassword(String loginId, String password);

}
