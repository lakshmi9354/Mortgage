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
public class MortgageDto implements Serializable{
	
	private static final long serialVersionUID = -5448760867603243245L;
	
	private String operationType;	
	private Double propertyCost;	
	private Double deposit;	
	private String employmentStatus;	
	private String occupation;	
	private String contractType;	
	private String dateOfJoining;	
	private String title;	
	private String firstName;	
	private String middleName;	
	private String surName;	
	private String dateOfBirth;	
	private Long phoneNumber;
	private String email;	
	private String confirmEmail;
}
