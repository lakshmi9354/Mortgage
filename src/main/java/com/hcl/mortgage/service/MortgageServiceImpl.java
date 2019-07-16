package com.hcl.mortgage.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.mortgage.dto.MortgageDetailsDto;
import com.hcl.mortgage.dto.MortgageDto;
import com.hcl.mortgage.entity.Account;
import com.hcl.mortgage.entity.Customer;
import com.hcl.mortgage.entity.Mortgage;
import com.hcl.mortgage.entity.Transaction;
import com.hcl.mortgage.exception.CustomerNotFoundException;
import com.hcl.mortgage.exception.MortgageException;
import com.hcl.mortgage.repository.AccountRepository;
import com.hcl.mortgage.repository.CustomerRepository;
import com.hcl.mortgage.repository.MortgageRepository;
import com.hcl.mortgage.repository.TransactionRepository;
import com.hcl.mortgage.util.PasswordEncoder;

@Service
public class MortgageServiceImpl implements IMortgageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MortgageServiceImpl.class);

	@Autowired
	MortgageRepository mortgageRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	TransactionRepository transactionRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	public MortgageDetailsDto createMortgage(MortgageDto mortgageDto) {
		LOGGER.debug("MortgageServiceImpl:createMortgage");

		Account transactionalAccount = null;
		Account mortgageAccount = null;
		Customer customer = null;
		Mortgage mortgage = null;
		Transaction transaction = null;
		Transaction mortgageTransaction = null;
		MortgageDetailsDto mortgageDetailsDto = null;

		String birthDay = mortgageDto.getDateOfBirth();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dob = LocalDate.parse(birthDay, formatter);

		if (mortgageDto.getPropertyCost() >= 100000 && mortgageDto.getDeposit() > 0) {
			if (nameValidation(mortgageDto.getFirstName()) && nameValidation(mortgageDto.getMiddleName())
					&& nameValidation(mortgageDto.getSurName())) {
				if (validPhoneNumber(mortgageDto.getPhoneNumber())) {
					if (emailValidation(mortgageDto.getEmail())
							&& mortgageDto.getEmail().equals(mortgageDto.getConfirmEmail())) {
						if (validAge(dob)) {
							
							mortgage = new Mortgage();
							customer = new Customer();
							transaction = new Transaction();
							mortgageTransaction = new Transaction();
							mortgageDetailsDto = new MortgageDetailsDto();
							Random random = new Random();
							
							customer.setLoginId(mortgageDto.getFirstName() + random.nextInt(1000));
							customer.setPassword(mortgageDto.getFirstName()+"@"+random.nextInt(100));
							customer.setCustomerName(mortgageDto.getFirstName() + random.nextInt(1000));
							
							customerRepository.save(customer);

							transactionalAccount = new Account();
							transactionalAccount.setBalance(mortgageDto.getPropertyCost() - mortgageDto.getDeposit());
							transactionalAccount.setAccountNumber("ACC"+random.nextInt(10000));
							transactionalAccount.setAccountType("Transactional Account");
							transactionalAccount.setDate(LocalDate.now());
							transactionalAccount.setCustomer(customer);

							accountRepository.save(transactionalAccount);

							mortgageAccount = new Account();
							mortgageAccount.setBalance(-(mortgageDto.getPropertyCost() - mortgageDto.getDeposit()));
							mortgageAccount.setAccountNumber("MORT"+random.nextInt(10000));
							mortgageAccount.setAccountType("Mortgage Account");
							mortgageAccount.setDate(LocalDate.now());
							mortgageAccount.setCustomer(customer);

							accountRepository.save(mortgageAccount);

							String joinDate = mortgageDto.getDateOfJoining();
							LocalDate doj = LocalDate.parse(joinDate, formatter);
							
							BeanUtils.copyProperties(mortgageDto, mortgage, "dateOfJoining", "dateOfBirth");
							mortgage.setDateOfBirth(dob);
							mortgage.setDateOfJoining(doj);
							mortgage.setCustomer(customer);
							mortgageRepository.save(mortgage);
							
							transaction.setFromAccount(transactionalAccount.getAccountNumber());
							transaction.setToAccount(mortgageAccount.getAccountNumber());
							transaction.setAmount(mortgageDto.getDeposit());
							transaction.setTransactionDate(LocalDate.now());
							transaction.setTransactionTime(LocalTime.now());
							transaction.setAccount(transactionalAccount);
							
							transactionRepository.save(transaction);
							
							mortgageTransaction.setFromAccount(transactionalAccount.getAccountNumber());
							mortgageTransaction.setToAccount(mortgageAccount.getAccountNumber());
							mortgageTransaction.setAmount(mortgageDto.getDeposit());
							mortgageTransaction.setTransactionDate(LocalDate.now());
							mortgageTransaction.setTransactionTime(LocalTime.now());
							mortgageTransaction.setAccount(mortgageAccount);
							transactionRepository.save(mortgageTransaction);
							
							mortgageDetailsDto.setLoginId(customer.getLoginId());
							mortgageDetailsDto.setPassword(customer.getPassword());
							mortgageDetailsDto.setAccountNumber(transactionalAccount.getAccountNumber());
							mortgageDetailsDto.setMortgageNumber(mortgageAccount.getAccountNumber());
							mortgageDetailsDto.setCustomerName(customer.getCustomerName());
							mortgageDetailsDto.setMessage("Congratulations, your mortgage has been granted.");

							return mortgageDetailsDto;
							
						} else {
							throw new MortgageException("You are not eligible for this Loan age should be >18");
						}

					} else {
						throw new MortgageException("Please Enter Valid Email and Email Conform Email Should be Same.");
					}

				} else {
					throw new MortgageException("Please Enter Valid Phone Number.");
				}
			}

			else {
				throw new MortgageException("FirstName or MiddleName or SurName Mut and sshould be alphabhets.");
			}
		}

		else {
			throw new MortgageException("Property Cost Minimum 1000000 or Deposit should Not be an Negative amount.");
		}
	}
	
	public String validateLogin(String loginId, String password) {
		LOGGER.debug("MortgageServiceImpl:validateLogin {} ", loginId);
		Optional<Customer> user = customerRepository.findByLoginIdAndPassword(loginId,password);
		if (user.isPresent()) {
			return "Logged in successfully.";
		} else {
			throw new CustomerNotFoundException("Invlid username or password");
		}
	}

	
	
	
	

	static boolean validPhoneNumber(Long number) {
		String num = number.toString();
		Pattern p = Pattern.compile("^[0-9]{10}$");
		Matcher m = p.matcher(num);
		return (m.find() && m.group().equals(num));
	}

	static boolean validAge(LocalDate date1) {
		boolean result = false;
		int birthYear = date1.getYear();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int age = year - birthYear;
		if (age > 18) {
			result = true;
		}
		return result;
	}

	static boolean emailValidation(String email) {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(regex);
	}

	static boolean nameValidation(String name) {
		String regex = "^[a-zA-Z]*$";
		return name.matches(regex);
	}
}
