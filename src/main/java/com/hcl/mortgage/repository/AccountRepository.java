package com.hcl.mortgage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcl.mortgage.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{

	@Query(value = "select a.* from account a,customer c where a.customer_id=:customerId and a.customer_id=c.customer_id",nativeQuery = true)
	List<Account> accountSummary(@Param("customerId") Long customerId);

	@Query(value = "select a.* from account a,customer c where a.customer_id=:customerId and a.customer_id=c.customer_id and a.account_type=:accountType",nativeQuery = true)
	Account findByCustomerIdAndAccountType(@Param("customerId") Long customerId, @Param("accountType") String accountType);

}
