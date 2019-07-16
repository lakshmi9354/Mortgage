package com.hcl.mortgage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcl.mortgage.entity.Mortgage;

@Repository
public interface MortgageRepository extends JpaRepository<Mortgage, Long>{

}
