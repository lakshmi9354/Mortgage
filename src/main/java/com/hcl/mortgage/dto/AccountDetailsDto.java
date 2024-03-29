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
public class AccountDetailsDto implements Serializable{

	
	private static final long serialVersionUID = -3146519735453477342L;
	
	 private String accountNumber;
	 private Double balance;
	 private String accountType;
	 private Long customerId;

}
