package com.hcl.mortgage.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Transaction implements Serializable {


	private static final long serialVersionUID = -875021029420471608L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long transactionId;
	private String fromAccount;
	private String toAccount;
	private Double amount;
	private String DrOrCr;
	private LocalDate transactionDate;
	private LocalTime transactionTime;

	@ManyToOne
	@JoinColumn(name = "accountId")
	private Account account;

}
