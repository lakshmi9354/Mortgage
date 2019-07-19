package com.hcl.mortgage.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

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
public class TransactionDetailsDto implements Serializable{
	
	private static final long serialVersionUID = 2657323616985524132L;
	
	private Double amount;
	private LocalDate transactionDate;
	private LocalTime transactionTime;
	
}
