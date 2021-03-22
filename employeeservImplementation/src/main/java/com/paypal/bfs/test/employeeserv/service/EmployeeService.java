package com.paypal.bfs.test.employeeserv.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.paypal.bfs.test.employeeserv.api.exception.InputFormatException;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.entity.AdressEntity;
import com.paypal.bfs.test.employeeserv.entity.EmployeeEntity;
import com.paypal.bfs.test.employeeserv.repository.AddressRepository;
import com.paypal.bfs.test.employeeserv.repository.EmployeeRepository;

@Service
public class EmployeeService {
	
	@Autowired
	EmployeeRepository empRepo;
	
	@Autowired
	AddressRepository adRepo;

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void addEmployee(AdressEntity addEnt, EmployeeEntity empEnt
			, Employee emp, DateTimeFormatter dtf) throws InputFormatException {
		
		if(addEnt != null) {
			AdressEntity adSaved = adRepo.saveAndFlush(addEnt);
			empEnt.setAddress(adSaved);
		}
		else empEnt.setAddress(null);
		
		if(emp.getDateOfBirth() != null)
			empEnt.setDateOfBirth(LocalDate.parse(emp.getDateOfBirth(), dtf));
		else empEnt.setDateOfBirth(null);
		
		if(emp.getFirstName() != null) empEnt.setFirstName(emp.getFirstName());
		else throw new InputFormatException("no first name specified");
		
		if(emp.getLastName() != null) empEnt.setLastName(emp.getLastName());
		else throw new InputFormatException("no last name specified");
		
		EmployeeEntity saved = empRepo.saveAndFlush(empEnt);
		
		emp.setId(saved.getId().intValue());
		
		emp.getAddress().setCity(saved.getAddress().getCity());
		emp.getAddress().setState(saved.getAddress().getState());
		emp.getAddress().setCountry(saved.getAddress().getCountry());
		emp.getAddress().setZipCode(saved.getAddress().getZipCode());
		emp.getAddress().setLine1(saved.getAddress().getFirstLine());
		emp.getAddress().setLine2(saved.getAddress().getSecondLine());
		emp.getAddress().setId(saved.getAddress().getId().intValue());
		
	}
	
}
