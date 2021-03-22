package com.paypal.bfs.test.employeeserv.unittest;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.paypal.bfs.test.employeeserv.impl.EmployeeResourceImpl;
import com.paypal.bfs.test.employeeserv.repository.EmployeeRepository;

public class CreateEmployeeTests {
	
	@InjectMocks
    private EmployeeResourceImpl employeeResourceImpl;

    @Mock
    private EmployeeRepository empRepo;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    

}
