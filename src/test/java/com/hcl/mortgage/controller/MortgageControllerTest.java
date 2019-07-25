package com.hcl.mortgage.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.mortgage.dto.AccountDetailsDto;
import com.hcl.mortgage.dto.LoginDto;
import com.hcl.mortgage.dto.MortgageDetailsDto;
import com.hcl.mortgage.dto.MortgageDto;
import com.hcl.mortgage.dto.TransactionDetailsDto;
import com.hcl.mortgage.entity.Customer;
import com.hcl.mortgage.service.MortgageServiceImpl;

@RunWith(SpringRunner.class)
@WebMvcTest(MortgageController.class)
public class MortgageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MortgageServiceImpl mortgageService;

	@Test
	public void accountSummarryTest() throws Exception {
		AccountDetailsDto accountDetailsDto = new AccountDetailsDto();
		accountDetailsDto.setBalance(3000.0);

		List<AccountDetailsDto> accountDetailsDtos = Arrays.asList(accountDetailsDto);

		Mockito.when(mortgageService.accountSummarry(Mockito.anyLong())).thenReturn(accountDetailsDtos);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/accountSummary/{customerId}", Mockito.anyInt()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$", hasSize(1))).andDo(print());
	}

	@Test
	public void transactionsTest() throws Exception {
		TransactionDetailsDto transactionDetailsDto = new TransactionDetailsDto();
		transactionDetailsDto.setAmount(20000.0);

		List<TransactionDetailsDto> transactionDetailsDtos = Arrays.asList(transactionDetailsDto);

		Mockito.when(mortgageService.transactions(Mockito.anyString())).thenReturn(transactionDetailsDtos);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/transactions/{accountNumber}", Mockito.anyInt()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$", hasSize(1))).andDo(print());

	}

	@Test
	public void valiateLoginTest() throws Exception {

		Customer customer = new Customer();
		customer.setCustomerId(1L);
		customer.setCustomerName("MP001");
		customer.setLoginId("LAK123");
		customer.setPassword("LAK@123");

		LoginDto loginDto = new LoginDto("LAK123", "LAK@123");

		AccountDetailsDto accountDetailsDto1 = new AccountDetailsDto();
		accountDetailsDto1.setAccountNumber("ACC123");
		accountDetailsDto1.setCustomerId(customer.getCustomerId());

		AccountDetailsDto accountDetailsDto2 = new AccountDetailsDto();
		accountDetailsDto2.setAccountNumber("MORT123");
		accountDetailsDto2.setCustomerId(customer.getCustomerId());

		List<AccountDetailsDto> accountDetailsDtos = Arrays.asList(accountDetailsDto1, accountDetailsDto2);

		Mockito.when(mortgageService.validateLogin(loginDto)).thenReturn(accountDetailsDtos);

		mockMvc.perform(MockMvcRequestBuilders.put("/api/login").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(loginDto))).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", hasSize(0))).andDo(print());

	}

	@Test
	public void createMortgageTest() throws Exception {
		MortgageDto mortgageDto = new MortgageDto();
		mortgageDto.setEmail("mpl@gmail.com");

		MortgageDetailsDto mortgageDetailsDto = new MortgageDetailsDto();
		mortgageDetailsDto.setAccountNumber("ACC123");

		Mockito.when(mortgageService.createMortgage(mortgageDto)).thenReturn(mortgageDetailsDto);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/mortgageSignup").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(mortgageDto))).andExpect(MockMvcResultMatchers.status().isCreated()).andDo(print());
	}
	
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
