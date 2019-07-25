package com.hcl.mortgage.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Serializable {

	private static final long serialVersionUID = 4325304850309000479L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long customerId;
	private String loginId;
	private String customerName;
	private String password;
	
	@OneToMany(mappedBy = "customer")
	private List<Account> account;
	
}