package com.paypal.bfs.test.employeeserv.unittest;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.paypal.bfs.test.employeeserv.api.exception.EmployeeNotFoundException;
import com.paypal.bfs.test.employeeserv.api.exception.InputFormatException;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.entity.AdressEntity;
import com.paypal.bfs.test.employeeserv.entity.EmployeeEntity;
import com.paypal.bfs.test.employeeserv.impl.EmployeeResourceImpl;
import com.paypal.bfs.test.employeeserv.repository.EmployeeRepository;

public class GetEmployeeTests {
	
	@InjectMocks
    private EmployeeResourceImpl employeeResourceImpl;

    @Mock
    private EmployeeRepository empRepo;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUserByIdwithAllNullValues() 
    		throws EmployeeNotFoundException, InputFormatException {
        EmployeeEntity emp = new EmployeeEntity();
        emp.setId(1l);
        emp.setFirstName("srinivas");
        emp.setLastName("vaidyanathan");
        Mockito.when(empRepo.findById(1l)).thenReturn(Optional.of(emp));

        ResponseEntity<Employee> res = employeeResourceImpl.employeeGetById("1");
        assertEquals(res.getBody().getFirstName(), "srinivas");
        assertEquals(res.getBody().getLastName(), "vaidyanathan");
        assertEquals(res.getBody().getAddress(), null);
    }
    
    @Test
    public void testGetUserByIdwithFullValues() 
    		throws EmployeeNotFoundException, InputFormatException {
        EmployeeEntity emp = new EmployeeEntity();
        AdressEntity address = new AdressEntity();
        address.setCity("test");
        address.setCountry("test");
        address.setFirstLine("test");
        address.setState("test");
        address.setZipCode(123);
        emp.setId(1l);
        emp.setFirstName("srinivas");
        emp.setLastName("vaidyanathan");
        emp.setDateOfBirth(LocalDate.now());
        emp.setAddress(address);
        Mockito.when(empRepo.findById(1l)).thenReturn(Optional.of(emp));

        ResponseEntity<Employee> res = employeeResourceImpl.employeeGetById("1");
        assertEquals(res.getBody().getFirstName(), "srinivas");
        assertEquals(res.getBody().getLastName(), "vaidyanathan");
        assertEquals(res.getBody().getAddress().getZipCode(), new Integer(123));
    }
    
    @Test
    public void testGetUserByIdwithNullRequiredAddressValues() 
    		throws EmployeeNotFoundException, InputFormatException {
        EmployeeEntity emp = new EmployeeEntity();
        AdressEntity address = new AdressEntity();
        address.setCity("test");
        address.setCountry("test");
        //address.setFirstLine("test"); -> removing required value
        address.setState("test");
        address.setZipCode(123);
        emp.setId(1l);
        emp.setFirstName("srinivas");
        emp.setLastName("vaidyanathan");
        emp.setDateOfBirth(LocalDate.now());
        emp.setAddress(address);
        Mockito.when(empRepo.findById(1l)).thenReturn(Optional.of(emp));

        ResponseEntity<Employee> res = employeeResourceImpl.employeeGetById("1");
        assertEquals(res.getBody().getFirstName(), "srinivas");
        assertEquals(res.getBody().getLastName(), "vaidyanathan");
        assertEquals(res.getBody().getAddress().getZipCode(), new Integer(123));
    }
    
    @Test(expected = InputFormatException.class)
    public void testGetUserByIdwithNullInput() 
    		throws EmployeeNotFoundException, InputFormatException {

        employeeResourceImpl.employeeGetById(null);
       
    }
    
    @Test(expected = EmployeeNotFoundException.class)
    public void testGetUserByIdwithNonExitingInput() 
    		throws EmployeeNotFoundException, InputFormatException {
    	
    	EmployeeEntity emp = new EmployeeEntity();
        emp.setId(1l);
        emp.setFirstName("srinivas");
        emp.setLastName("vaidyanathan");
        Mockito.when(empRepo.findById(1l)).thenReturn(Optional.of(emp));
        employeeResourceImpl.employeeGetById("2");
       
    }
    
    

}
