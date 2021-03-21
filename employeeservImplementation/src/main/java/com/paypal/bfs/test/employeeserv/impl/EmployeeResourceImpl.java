package com.paypal.bfs.test.employeeserv.impl;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.exception.AddressNotFoundException;
import com.paypal.bfs.test.employeeserv.api.exception.EmployeeIdFoundException;
import com.paypal.bfs.test.employeeserv.api.exception.EmployeeNotFoundException;
import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.entity.AdressEntity;
import com.paypal.bfs.test.employeeserv.entity.EmployeeEntity;
import com.paypal.bfs.test.employeeserv.repository.AddressRepository;
import com.paypal.bfs.test.employeeserv.repository.EmployeeRepository;
import com.paypal.bfs.test.employeeserv.service.EmployeeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementation class for employee resource.
 */
@RestController
public class EmployeeResourceImpl implements EmployeeResource {
	
	@Autowired
	EmployeeRepository empRepo;
	
	@Autowired
	AddressRepository adRepo;
	
	@Autowired
	EmployeeService empSer;
	
	final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public ResponseEntity<Employee> employeeGetById(String id) 
    		throws EmployeeNotFoundException {
    	
    	Optional<EmployeeEntity> ent = empRepo.findById(Long.parseLong(id));
    	
    	if(!ent.isPresent()) 
    		throw new EmployeeNotFoundException("the employee with id "+id
    				+" does not exist");
    	
    	Employee emp = new Employee();
    	Address addr = new Address();
    	addr.setCity(ent.get().getAddress().getCity());
    	addr.setCountry(ent.get().getAddress().getCountry());
    	addr.setLine1(ent.get().getAddress().getFirstLine());
    	addr.setLine2(ent.get().getAddress().getSecondLine());
    	addr.setState(ent.get().getAddress().getState());
    	addr.setZipCode(ent.get().getAddress().getZipCode());
    	emp.setAddress(addr);
    	emp.setDateOfBirth(ent.get().getDateOfBirth().format(dtf));
    	emp.setFirstName(ent.get().getFirstName());
    	emp.setLastName(ent.get().getLastName());
    	emp.setId(ent.get().getId().intValue());
    	
        return new ResponseEntity<>(emp, HttpStatus.OK);
    }

	@Override
	public ResponseEntity<Employee> createEmployee(Employee emp) 
			throws EmployeeIdFoundException, AddressNotFoundException {
		
		/*
		 * for employee creation the edge case handling is as follows.
		 * 1. if the employee contains an id then the error response is returned.
		 * 2. if there is no id then a new employee is created.
		 * 3. if address has an id then the rest of the address is ignored and 
		 * the employee that is created is linked to the address in the id.
		 * 4. if address does not have an id then a new address row is created.
		 * 5. if address has an id that does not exist then exception is thrown.
		 */
		
		EmployeeEntity empEnt = new EmployeeEntity();
		if(emp.getId() != null) {
			throw new EmployeeIdFoundException("found id "+emp.getId()+" with employee "
					+ "creation request, this field is not allowed while creating "
					+ "a record");
		}
		
		AdressEntity addEnt = new AdressEntity();
		
		if(emp.getAddress().getId() != null) {
			Optional<AdressEntity> adent = adRepo.findById(emp.getAddress().getId()*1L);
			if(!adent.isPresent()) {
				throw new AddressNotFoundException("the address with id "
						+ emp.getAddress().getId()+" does not exist on the db "
								+ "if you want to add new address, remove the id field"
								+ " in the address");
			}
		}
		else {
			addEnt.setFirstLine(emp.getAddress().getLine1());
			addEnt.setSecondLine(emp.getAddress().getLine2());
			addEnt.setCity(emp.getAddress().getCity());
			addEnt.setState(emp.getAddress().getState());
			addEnt.setCountry(emp.getAddress().getCountry());
			addEnt.setZipCode(emp.getAddress().getZipCode());
		}
		
		empSer.addEmployee(addEnt, empEnt, emp, dtf);
		
		return new ResponseEntity<>(emp, HttpStatus.OK);

	}
}
