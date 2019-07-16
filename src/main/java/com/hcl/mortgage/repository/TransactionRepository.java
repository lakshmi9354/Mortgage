package com.hcl.mortgage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcl.mortgage.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{

}
