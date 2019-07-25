package com.hcl.mortgage.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MortgageDetailsDto implements Serializable{

	private static final long serialVersionUID = 21118504072999442L;
	
	private String loginId;
	private String password;
	private String mortgageNumber;
	private String customerName;
	private String accountNumber;
	private String message;
}
