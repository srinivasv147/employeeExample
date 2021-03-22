package com.paypal.bfs.test.employeeserv.unittest;

import static org.junit.Assert.assertEquals;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.paypal.bfs.test.employeeserv.api.exception.AddressNotFoundException;
import com.paypal.bfs.test.employeeserv.api.exception.EmployeeIdFoundException;
import com.paypal.bfs.test.employeeserv.api.exception.EmployeeNotFoundException;
import com.paypal.bfs.test.employeeserv.api.exception.InputFormatException;
import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.entity.AdressEntity;
import com.paypal.bfs.test.employeeserv.entity.EmployeeEntity;
import com.paypal.bfs.test.employeeserv.impl.EmployeeResourceImpl;
import com.paypal.bfs.test.employeeserv.repository.AddressRepository;
import com.paypal.bfs.test.employeeserv.service.EmployeeService;

public class CreateEmployeeTests {
	
	@InjectMocks
    private EmployeeResourceImpl employeeResourceImpl;
    
    @Mock
    private EmployeeService empSer;
    
    @Mock
    private AddressRepository adRepo;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    @Test(expected = InputFormatException.class)
    public void testCreateUserWithNull() 
    		throws EmployeeNotFoundException
    		, InputFormatException
    		, EmployeeIdFoundException, AddressNotFoundException {
        Employee emp = null;
        employeeResourceImpl.createEmployee(emp);
        
    }
    
    @Test(expected = EmployeeIdFoundException.class)
    public void testCreateUserWithUserId() 
    		throws EmployeeNotFoundException
    		, InputFormatException
    		, EmployeeIdFoundException, AddressNotFoundException {
        Employee emp = new Employee();
        emp.setId(1);
        employeeResourceImpl.createEmployee(emp);
        
    }
    
    @Test
    public void testCreateUserWithNullAddress() 
    		throws EmployeeNotFoundException
    		, InputFormatException
    		, EmployeeIdFoundException, AddressNotFoundException {
    	
        Employee emp = new Employee();
        emp.setFirstName("test");
        emp.setLastName("test");
        
        Mockito.doAnswer((i) -> { 
        	((Employee)i.getArgument(2)).setId(1);
        	return null;})
        .when(empSer).addEmployee(
        		Mockito.isNull()
				, Mockito.any(EmployeeEntity.class)
				, Mockito.any(Employee.class)
				, Mockito.any(DateTimeFormatter.class));
        
        ResponseEntity<Employee> res = employeeResourceImpl.createEmployee(emp);
        
        Mockito.verify(empSer, Mockito.times(1)).addEmployee(
        		Mockito.isNull()
				, Mockito.any(EmployeeEntity.class)
				, Mockito.any(Employee.class)
				, Mockito.any(DateTimeFormatter.class));
        
        assertEquals(res.getBody().getId(), new Integer(1));
        
    }
    
    @Test(expected = AddressNotFoundException.class)
    public void testCreateUserWithNonExistingAddressId() 
    		throws EmployeeNotFoundException
    		, InputFormatException
    		, EmployeeIdFoundException, AddressNotFoundException {
    	Employee emp = new Employee();
        emp.setFirstName("test");
        emp.setLastName("test");
        emp.setAddress(new Address());
        emp.getAddress().setId(1);
        
        Mockito.when(adRepo.findById(1l)).thenReturn(Optional.empty());
        
        employeeResourceImpl.createEmployee(emp);
        
    }
    
    @Test
    public void testCreateUserWithExistingAddressId() 
    		throws EmployeeNotFoundException
    		, InputFormatException
    		, EmployeeIdFoundException, AddressNotFoundException {
    	Employee emp = new Employee();
        emp.setFirstName("test");
        emp.setLastName("test");
        emp.setAddress(new Address());
        emp.getAddress().setId(1);
        emp.getAddress().setCity("test");
        emp.getAddress().setState("test");
        emp.getAddress().setCountry("test");
        emp.getAddress().setLine1("test");
        emp.getAddress().setZipCode(123);
        
        AdressEntity ad = new AdressEntity();
        ad.setCity("");
        ad.setCountry("");
        ad.setFirstLine("");
        ad.setState("");
        ad.setZipCode(123);
        ad.setId(1l);
        
        Mockito.doAnswer((i) -> { 
        	((Employee)i.getArgument(2)).setId(1);
        	((Employee)i.getArgument(2)).getAddress().setCity(ad.getCity());
        	((Employee)i.getArgument(2)).getAddress().setCountry(ad.getCountry());
        	((Employee)i.getArgument(2)).getAddress().setState(ad.getState());
        	((Employee)i.getArgument(2)).getAddress().setZipCode(ad.getZipCode());
        	((Employee)i.getArgument(2)).getAddress().setId(ad.getId().intValue());
        	((Employee)i.getArgument(2)).getAddress().setLine1(ad.getFirstLine());
        	((Employee)i.getArgument(2)).getAddress().setLine2(ad.getSecondLine());
        	return null;})
        .when(empSer).addEmployee(
        		Mockito.any(AdressEntity.class)
				, Mockito.any(EmployeeEntity.class)
				, Mockito.any(Employee.class)
				, Mockito.any(DateTimeFormatter.class));
        
        Mockito.when(adRepo.findById(1l)).thenReturn(Optional.of(ad));
        
        ResponseEntity<Employee> res = employeeResourceImpl.createEmployee(emp);
        
        assertEquals(res.getBody().getAddress().getId(), new Integer(1));
        assertEquals(res.getBody().getId(), new Integer(1));
        assertEquals(res.getBody().getAddress().getCity(), "");
        
    }
    
    @Test
    public void testCreateUserWithNewAddress() 
    		throws EmployeeNotFoundException
    		, InputFormatException
    		, EmployeeIdFoundException, AddressNotFoundException {
    	Employee emp = new Employee();
        emp.setFirstName("test");
        emp.setLastName("test");
        emp.setAddress(new Address());
        emp.getAddress().setCity("test");
        emp.getAddress().setState("test");
        emp.getAddress().setCountry("test");
        emp.getAddress().setLine1("test");
        emp.getAddress().setZipCode(123);
        
        Mockito.doAnswer((i) -> { 
        	((Employee)i.getArgument(2)).setId(1);
        	((Employee)i.getArgument(2)).getAddress().setCity("test");
        	((Employee)i.getArgument(2)).getAddress().setCountry("test");
        	((Employee)i.getArgument(2)).getAddress().setState("test");
        	((Employee)i.getArgument(2)).getAddress().setZipCode(123);
        	((Employee)i.getArgument(2)).getAddress().setId(1);
        	((Employee)i.getArgument(2)).getAddress().setLine1("test");
        	((Employee)i.getArgument(2)).getAddress().setLine2("test");
        	return null;})
        .when(empSer).addEmployee(
        		Mockito.any(AdressEntity.class)
				, Mockito.any(EmployeeEntity.class)
				, Mockito.any(Employee.class)
				, Mockito.any(DateTimeFormatter.class));
        
        ResponseEntity<Employee> res = employeeResourceImpl.createEmployee(emp);
        
        assertEquals(res.getBody().getId(), new Integer(1));
        assertEquals(res.getBody().getAddress().getId(), new Integer(1));
        assertEquals(res.getBody().getAddress().getCity(), "test");
        
    }

}
