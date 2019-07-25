package com.hcl.mortgage.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.hcl.mortgage.dto.AccountDetailsDto;
import com.hcl.mortgage.dto.LoginDto;
import com.hcl.mortgage.dto.MortgageDetailsDto;
import com.hcl.mortgage.dto.MortgageDto;
import com.hcl.mortgage.dto.TransactionDetailsDto;
import com.hcl.mortgage.entity.Account;
import com.hcl.mortgage.entity.Customer;
import com.hcl.mortgage.entity.Mortgage;
import com.hcl.mortgage.entity.Transaction;
import com.hcl.mortgage.exception.CustomerNotFoundException;
import com.hcl.mortgage.repository.AccountRepository;
import com.hcl.mortgage.repository.CustomerRepository;
import com.hcl.mortgage.repository.MortgageRepository;
import com.hcl.mortgage.repository.TransactionRepository;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MortgageServiceTest {

	@Autowired
	MortgageServiceImpl mortgageService;
	
	@MockBean
	MortgageRepository mortgageRepository;
	
	@MockBean
	AccountRepository accountRepository;
	
	@MockBean
	CustomerRepository customerRepository;
	
	@MockBean
	TransactionRepository transactionRepository;
	
	@Mock
	Account mortgagAccount;
	
	List<Account> accounts = null;
	List<Transaction> transactions = null;
	List<AccountDetailsDto> accountDetailsDtos = null;
	List<TransactionDetailsDto> transactionDetailsDtos = null;
	
	Account transactionalAccount = null;
	Account mortgageAccount = null;
	Customer customer = null;
	Mortgage mortgage = null;
	MortgageDto mortgageDto = null;
	Transaction transaction = null;
	Transaction mortgageTransaction = null;
	MortgageDetailsDto mortgageDetailsDto = null;
	LoginDto loginDto = null;
	AccountDetailsDto accountDetailsDto = null;
	TransactionDetailsDto transacinDetailsDto = null;
	
	
	@Test
	public void craeteMortgageTestSuccess() {
		System.out.println("AccountNuber: "+mortgagAccount.getAccountNumber());
		transactionalAccount = new Account();
		mortgageAccount = new Account();
		customer = new Customer();
		mortgage = new Mortgage();
		transaction = new Transaction();
		mortgageDto = new MortgageDto();
		mortgageDetailsDto = new MortgageDetailsDto();
		
		mortgageDto.setEmail("mpl@gmail.com");
		mortgageDto.setDateOfBirth("1991-06-18");
		mortgageDto.setDateOfJoining("1991-06-18");
		mortgageDto.setConfirmEmail("mpl@gmail.com");
		mortgageDto.setContractType("A");
		mortgageDto.setDeposit(20000.0);
		mortgageDto.setEmploymentStatus("SDB");
		mortgageDto.setFirstName("FRT");
		mortgageDto.setMiddleName("DSC");
		mortgageDto.setOccupation("DSCF");
		mortgageDto.setPhoneNumber(9618339354L);
		mortgageDto.setPropertyCost(1000000.0);
		mortgageDto.setSurName("DSC");
		mortgageDto.setTitle("Mr");
		
		//Mockito.when(mortgageRepository.save(mortgage)).thenReturn(mortgage);
		//Mockito.when(accountRepository.save(transactionalAccount)).thenReturn(transactionalAccount);
		//Mockito.when(accountRepository.save(mortgageAccount)).thenReturn(mortgageAccount);
		//Mockito.when(customerRepository.save(customer)).thenReturn(customer);
		//Mockito.when(transactionRepository.save(transaction)).thenReturn(transaction);
		//assertEquals(mortgageDetailsDto.toString(), mortgageService.createMortgage(mortgageDto).toString());
		
	}
	
	@Test
	public void validateLoginTest() {
		loginDto = new LoginDto();
		loginDto.setLoginId("LAK123");
		loginDto.setPassword("LAK@12");
		
		customer = new Customer();
		customer.setLoginId("LAK123");
		customer.setPassword("LAK@12");
		customer.setCustomerId(1L);
		
		Account account = new Account();
		account.setAccountNumber("SCC123");
		
		accounts = new ArrayList<>();
		accounts.add(account);
		
		accountDetailsDto = new AccountDetailsDto();
		accountDetailsDtos = new ArrayList<AccountDetailsDto>();
		accountDetailsDto.setCustomerId(customer.getCustomerId());
		accountDetailsDto.setAccountNumber("SCC123");
		accountDetailsDtos.add(accountDetailsDto);
		
		Mockito.when(customerRepository.findByLoginIdAndPassword(loginDto.getLoginId(), loginDto.getPassword())).thenReturn(customer);
		Mockito.when(accountRepository.accountSummary(customer.getCustomerId())).thenReturn(accounts);
		assertEquals(accountDetailsDtos.size(), mortgageService.validateLogin(loginDto).size());
	}
	
	@Test(expected = CustomerNotFoundException.class)
	public void validateLoginTestNull() {
		loginDto = new LoginDto();
		loginDto.setLoginId("LA123");
		loginDto.setPassword("LAK@12");
		
		customer = new Customer();
		customer.setLoginId("LAK123");
		customer.setPassword("LAK@12");
		customer.setCustomerId(1L);
		
		Account account = new Account();
		account.setAccountNumber("SCC123");
		
		accounts = new ArrayList<>();
		accounts.add(account);
		
		accountDetailsDto = new AccountDetailsDto();
		accountDetailsDtos = new ArrayList<AccountDetailsDto>();
		accountDetailsDto.setCustomerId(customer.getCustomerId());
		accountDetailsDto.setAccountNumber("SCC123");
		accountDetailsDtos.add(accountDetailsDto);
		
		Mockito.when(customerRepository.findByLoginIdAndPassword(loginDto.getLoginId(), loginDto.getPassword())).thenReturn(null);
		assertEquals(Mockito.anyString(), mortgageService.validateLogin(loginDto));
	}
	
	@Test
	public void transactionsTest() {
		transaction = new Transaction();
		transaction.setAmount(20000.0);
		
		transactions = new ArrayList<>();
		transactions.add(transaction);
		
		transacinDetailsDto = new TransactionDetailsDto();
		transacinDetailsDto.setAmount(2000.0);
		
		transactionDetailsDtos = new ArrayList<>();
		transactionDetailsDtos.add(transacinDetailsDto);
		
		Mockito.when(transactionRepository.transactions(Mockito.anyString())).thenReturn(transactions);
		assertEquals(transactionDetailsDtos.size(), mortgageService.transactions(Mockito.anyString()).size());
	}
}
