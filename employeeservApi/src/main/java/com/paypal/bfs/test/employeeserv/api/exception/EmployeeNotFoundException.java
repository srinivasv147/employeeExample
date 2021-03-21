package com.paypal.bfs.test.employeeserv.api.exception;

public class EmployeeNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EmployeeNotFoundException(String errorMessage) {
		super(errorMessage);
	}

}
