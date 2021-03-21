package com.paypal.bfs.test.employeeserv.api.exception;

public class AddressNotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AddressNotFoundException(String errorMessage) {
		super(errorMessage);
	}

}
