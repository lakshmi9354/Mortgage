package com.hcl.mortgage.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.hcl.mortgage.dto.AccountDetailsDto;
import com.hcl.mortgage.dto.MortgageDetailsDto;
import com.hcl.mortgage.dto.MortgageDto;
import com.hcl.mortgage.dto.TransactionDetailsDto;
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
							customer.setPassword(mortgageDto.getFirstName() + "@" + random.nextInt(100));
							customer.setCustomerName(mortgageDto.getFirstName() + random.nextInt(1000));

							customerRepository.save(customer);

							transactionalAccount = new Account();
							transactionalAccount.setBalance(mortgageDto.getPropertyCost() - mortgageDto.getDeposit());
							transactionalAccount.setAccountNumber("ACC" + random.nextInt(10000));
							transactionalAccount.setAccountType("Transactional Account");
							transactionalAccount.setDate(LocalDate.now());
							transactionalAccount.setCustomer(customer);

							accountRepository.save(transactionalAccount);

							mortgageAccount = new Account();
							mortgageAccount.setBalance(-(mortgageDto.getPropertyCost() - mortgageDto.getDeposit()));
							mortgageAccount.setAccountNumber("MORT" + random.nextInt(10000));
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
							transaction.setDrOrCr("debit");
							transaction.setAmount(mortgageDto.getDeposit());
							transaction.setTransactionDate(LocalDate.now());
							transaction.setTransactionTime(LocalTime.now());
							transaction.setAccount(transactionalAccount);

							transactionRepository.save(transaction);

							mortgageTransaction.setToAccount(mortgageAccount.getAccountNumber());
							mortgageTransaction.setDrOrCr("credit");
							mortgageTransaction.setAmount(mortgageDto.getDeposit());
							mortgageTransaction.setTransactionDate(LocalDate.now());
							mortgageTransaction.setTransactionTime(LocalTime.now());
							mortgageTransaction.setAccount(mortgageAccount);
							transactionRepository.save(mortgageTransaction);

							mortgageDetailsDto.setLoginId(customer.getLoginId());
							mortgageDetailsDto.setPassword(customer.getPassword());
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
		Optional<Customer> user = customerRepository.findByLoginIdAndPassword(loginId, password);
		if (user.isPresent()) {
			return "Logged in successfully.";
		} else {
			throw new CustomerNotFoundException("Invlid username or password");
		}
	}

	public List<AccountDetailsDto> accountSummarry(Long customerId) {
		List<Account> accounts = accountRepository.accountSummary(customerId);
		System.out.println("111111" + accounts.size());
		List<AccountDetailsDto> accountDetailsDtos = new ArrayList<AccountDetailsDto>();
		AccountDetailsDto accountDetailsDto = null;
		for (Account account : accounts) {
			accountDetailsDto = new AccountDetailsDto();
			BeanUtils.copyProperties(account, accountDetailsDto);
			accountDetailsDtos.add(accountDetailsDto);
		}
		return accountDetailsDtos;
	}

	public List<TransactionDetailsDto> transactions(String acountNumber) {
		List<Transaction> transactions = transactionRepository.transactions(acountNumber);
		List<TransactionDetailsDto> transactionDetailsDtos = new ArrayList<TransactionDetailsDto>();
		TransactionDetailsDto transactionDetailsDto = null;
		for (Transaction account : transactions) {
			transactionDetailsDto = new TransactionDetailsDto();
			BeanUtils.copyProperties(account, transactionDetailsDto);
			transactionDetailsDtos.add(transactionDetailsDto);
		}
		return transactionDetailsDtos;
	}

	public String monthlyPayment() {
		List<Account> getAllAccounts = getAllAccounts();
		if (!(getAllAccounts.isEmpty())) {
			Long previousCustomerId = 0l;
			Long currentCustomerId = 0l;
			for (Account account : getAllAccounts) {
				Long customerId = account.getCustomer().getCustomerId();
				currentCustomerId = customerId;
				if (currentCustomerId != previousCustomerId) {
					Account transactionalAccount = getAccount(customerId, "Transactional Account");
					Account mortgageAccount = getAccount(customerId, "Mortgage Account");
					if (transactionalAccount.getBalance() >= 200) {
						if (mortgageAccount.getBalance() < 0) {
							Double transactionalAccountBalance = transactionalAccount.getBalance() - 200;
							Double mortgageAccountBalance = mortgageAccount.getBalance() + 200;
							transactionalAccount.setBalance(transactionalAccountBalance);
							mortgageAccount.setBalance(mortgageAccountBalance);
							save(transactionalAccount);
							save(mortgageAccount);

							Transaction transactionInTransactional = new Transaction();
							Transaction transactionInMortgage = new Transaction();

							transactionInTransactional.setAccount(transactionalAccount);
							transactionInTransactional.setAmount(200d);
							transactionInTransactional.setFromAccount(transactionalAccount.getAccountNumber());
							transactionInTransactional.setTransactionDate(LocalDate.now());
							transactionInTransactional.setTransactionTime(LocalTime.now());
							transactionInTransactional.setDrOrCr("debit");

							transactionInMortgage.setAccount(mortgageAccount);
							transactionInMortgage.setAmount(200d);
							transactionInMortgage.setToAccount(mortgageAccount.getAccountNumber());
							transactionInMortgage.setTransactionDate(LocalDate.now());
							transactionInMortgage.setTransactionTime(LocalTime.now());
							transactionInMortgage.setDrOrCr("credit");

							transactionRepository.save(transactionInTransactional);
							transactionRepository.save(transactionInMortgage);

							previousCustomerId = currentCustomerId;
						}
					}
				}
			}

			return "Batch Updated Successfully.";
		} else {
			return "Batch Failed";
		}
	}

	public List<Account> getAllAccounts() {
		List<Account> accounts = accountRepository.findAll();
		return accounts;
	}

	@Scheduled(fixedRate = 1 * 60 * 1000)
	public void testSchedule() {
		monthlyPayment();

	}

	public Account getAccount(Long customerId, String accountType) {
		Account account = accountRepository.findByCustomerIdAndAccountType(customerId, accountType);
		return account;

	}

	public Account save(Account account) {
		Account responseAccount = accountRepository.save(account);
		if (responseAccount != null) {
			return responseAccount;
		} else {
			return null;
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
