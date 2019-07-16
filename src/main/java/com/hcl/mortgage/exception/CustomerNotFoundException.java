package com.hcl.mortgage.exception;

public class CustomerNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3773462224171464013L;
	
	public CustomerNotFoundException(String message) {
		super(message);
	}

}
