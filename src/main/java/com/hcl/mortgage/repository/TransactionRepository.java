package com.hcl.mortgage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcl.mortgage.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{

	@Query(value = "select t.* from transaction t where t.from_account=:accountNumber or to_account=:accountNumber",nativeQuery = true)
	List<Transaction> transactions(@Param("accountNumber") String acountNumber);

}
