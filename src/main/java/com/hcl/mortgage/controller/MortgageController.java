package com.hcl.mortgage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.mortgage.dto.MortgageDetailsDto;
import com.hcl.mortgage.dto.MortgageDto;
import com.hcl.mortgage.service.IMortgageService;
import com.hcl.mortgage.service.MortgageServiceImpl;

@RestController
@RequestMapping("/api")
public class MortgageController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MortgageServiceImpl.class);
	
	@Autowired
	IMortgageService mortgageService;
	
	@PostMapping("/mortgageSignup")
	public ResponseEntity<MortgageDetailsDto> createMortgage(@RequestBody MortgageDto mortgageDto){
		LOGGER.debug("MortgageController:createMortgage");
		MortgageDetailsDto response = mortgageService.createMortgage(mortgageDto);
		return new ResponseEntity<MortgageDetailsDto>(response,HttpStatus.CREATED);
	}
	
	@PutMapping("/login")
	public ResponseEntity<String> validateLogin(String loginId, String password) {
		LOGGER.debug("MortgageController:validateLogin {} ", loginId);
		String response = mortgageService.validateLogin(loginId, password);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
