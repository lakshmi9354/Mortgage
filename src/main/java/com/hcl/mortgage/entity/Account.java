package com.hcl.mortgage.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Account implements Serializable {

	private static final long serialVersionUID = 8920625237783059741L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long accountId;
	private String accountNumber;
	private String accountType;
	private Double balance;
	private LocalDate date;
	@ManyToOne
	@JoinColumn(name = "customerId")
	private Customer customer;
	@OneToMany(mappedBy = "account")
	private List<Transaction> transactions;

}