package com.paypal.bfs.test.employeeserv.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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
			, Employee emp, DateTimeFormatter dtf) {
		
		AdressEntity adSaved = adRepo.saveAndFlush(addEnt);
		
		empEnt.setAddress(adSaved);
		empEnt.setDateOfBirth(LocalDate.parse(emp.getDateOfBirth(), dtf));
		empEnt.setFirstName(emp.getFirstName());
		empEnt.setLastName(emp.getLastName());
		
		EmployeeEntity saved = empRepo.saveAndFlush(empEnt);
		
		emp.setId(saved.getId().intValue());
		
	}
	
}
