package com.hcl.mortgage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcl.mortgage.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{

}
