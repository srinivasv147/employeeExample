package com.paypal.bfs.test.employeeserv.exhandler;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.paypal.bfs.test.employeeserv.api.exception.AddressNotFoundException;
import com.paypal.bfs.test.employeeserv.api.exception.EmployeeIdFoundException;
import com.paypal.bfs.test.employeeserv.api.exception.EmployeeNotFoundException;
import com.paypal.bfs.test.employeeserv.api.exception.InputFormatException;

@ControllerAdvice
public class ControllerExceptionHandler {
	
	@ExceptionHandler(EmployeeIdFoundException.class)
    public void springHandleEmpIdFound(HttpServletResponse response) 
    		throws IOException {
        response.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }
	
	@ExceptionHandler(AddressNotFoundException.class)
    public void springHandleAddressNotFound(HttpServletResponse response) 
    		throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }
	
	@ExceptionHandler(EmployeeNotFoundException.class)
    public void springHandleNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }
	
	@ExceptionHandler(InputFormatException.class)
    public void springHandleInputFormat(HttpServletResponse response) 
    		throws IOException {
        response.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

}
