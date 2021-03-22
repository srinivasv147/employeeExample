package com.paypal.bfs.test.employeeserv.impl;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.exception.AddressNotFoundException;
import com.paypal.bfs.test.employeeserv.api.exception.EmployeeIdFoundException;
import com.paypal.bfs.test.employeeserv.api.exception.EmployeeNotFoundException;
import com.paypal.bfs.test.employeeserv.api.exception.InputFormatException;
import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.entity.AdressEntity;
import com.paypal.bfs.test.employeeserv.entity.EmployeeEntity;
import com.paypal.bfs.test.employeeserv.repository.AddressRepository;
import com.paypal.bfs.test.employeeserv.repository.EmployeeRepository;
import com.paypal.bfs.test.employeeserv.service.EmployeeService;

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
    		throws EmployeeNotFoundException, InputFormatException {
    	
    	/*
    	 * 1. point to note here is if any required filed in employee is empty
    	 * in the database, we do not return it.
    	 *  
    	 * 2. The assumption is that the required fields are required only 
    	 * from the client side
    	 */
    	
    	if(id == null) throw new InputFormatException("id field is not valid");
    	
    	Optional<EmployeeEntity> ent = empRepo.findById(Long.parseLong(id));
    	
    	if(!ent.isPresent()) 
    		throw new EmployeeNotFoundException("the employee with id "+id
    				+" does not exist");
    	
    	Employee emp = new Employee();
    	Address addr = new Address();
    	
    	if(ent.get().getAddress() != null) {
    		addr.setCity(ent.get().getAddress().getCity());
        	addr.setCountry(ent.get().getAddress().getCountry());
        	addr.setLine1(ent.get().getAddress().getFirstLine());
        	addr.setLine2(ent.get().getAddress().getSecondLine());
        	addr.setState(ent.get().getAddress().getState());
        	addr.setZipCode(ent.get().getAddress().getZipCode());
    	}
    	else addr = null;
    	
    	emp.setAddress(addr);
    	if(ent.get().getDateOfBirth() != null) 
    		emp.setDateOfBirth(ent.get().getDateOfBirth().format(dtf));
    	else emp.setDateOfBirth(null);
    	emp.setFirstName(ent.get().getFirstName());
    	emp.setLastName(ent.get().getLastName());
    	emp.setId(ent.get().getId().intValue());
    	
        return new ResponseEntity<>(emp, HttpStatus.OK);
    }

	@Override
	public ResponseEntity<Employee> createEmployee(Employee emp) 
			throws EmployeeIdFoundException, AddressNotFoundException
			, InputFormatException {
		
		/*
		 * for employee creation the edge case handling is as follows.
		 * 1. if the employee contains an id then the error response is returned.
		 * 2. if there is no id then a new employee is created.
		 * 3. if address has an id then the rest of the address is ignored and 
		 * the employee that is created is linked to the address in the id.
		 * 4. if address does not have an id then a new address row is created.
		 * 5. if address has an id that does not exist then exception is thrown.
		 */
		
		if(emp == null) 
			throw new InputFormatException("could not read input employee");
		
		EmployeeEntity empEnt = new EmployeeEntity();
		
		if(emp.getId() != null) {
			throw new EmployeeIdFoundException("found id "+emp.getId()+" with employee "
					+ "creation request, this field is not allowed while creating "
					+ "a record");
		}
		
		AdressEntity addEnt = new AdressEntity();
		
		if(emp.getAddress() == null) {
			empSer.addEmployee(null, empEnt, emp, dtf);
			return new ResponseEntity<>(emp, HttpStatus.OK);
		}
		
		if(emp.getAddress().getId() != null) {
			Optional<AdressEntity> adent = adRepo.findById(emp.getAddress().getId()*1L);
			if(!adent.isPresent()) {
				throw new AddressNotFoundException("the address with id "
						+ emp.getAddress().getId()+" does not exist on the db "
								+ "if you want to add new address, remove the id field"
								+ " in the address");
			}
			addEnt = adent.get();
		}
		else {
			if(emp.getAddress().getLine1() != null)
				addEnt.setFirstLine(emp.getAddress().getLine1());
			else throw new InputFormatException("line 1 of address is mandatory");
			addEnt.setSecondLine(emp.getAddress().getLine2());
			if(emp.getAddress().getCity() != null)
				addEnt.setCity(emp.getAddress().getCity());
			else
				throw new InputFormatException("city field of address is mandatory");
			if(emp.getAddress().getState() != null)
				addEnt.setState(emp.getAddress().getState());
			else
				throw new InputFormatException("state field of address is mandatory");
			if(emp.getAddress().getCountry() != null)
				addEnt.setCountry(emp.getAddress().getCountry());
			else
				throw new InputFormatException("country field of address is madatory");
			if(emp.getAddress().getZipCode() != null)
				addEnt.setZipCode(emp.getAddress().getZipCode());
			else
				throw new InputFormatException("zip-code filed of address is madatory");
		}

		empSer.addEmployee(addEnt, empEnt, emp, dtf);
		
		return new ResponseEntity<>(emp, HttpStatus.OK);

	}
}
