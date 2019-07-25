package com.hcl.mortgage.service;

import java.util.List;

import com.hcl.mortgage.dto.AccountDetailsDto;
import com.hcl.mortgage.dto.LoginDto;
import com.hcl.mortgage.dto.MortgageDetailsDto;
import com.hcl.mortgage.dto.MortgageDto;
import com.hcl.mortgage.dto.TransactionDetailsDto;
import com.hcl.mortgage.entity.Account;

public interface IMortgageService {
	public MortgageDetailsDto createMortgage(MortgageDto mortgageDto);
	public List<AccountDetailsDto> validateLogin(LoginDto loginDto);
	public List<AccountDetailsDto> accountSummarry(Long customerId);
	public List<TransactionDetailsDto> transactions(String acountNumber);
	public Account getAccount(Long customerId, String string);
	public List<Account> getAllAccounts();
	public Account save(Account account);
	public String monthlyPayment();
}
