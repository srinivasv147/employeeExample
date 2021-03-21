package com.paypal.bfs.test.employeeserv.api;

import com.paypal.bfs.test.employeeserv.api.exception.AddressNotFoundException;
import com.paypal.bfs.test.employeeserv.api.exception.EmployeeIdFoundException;
import com.paypal.bfs.test.employeeserv.api.exception.EmployeeNotFoundException;
import com.paypal.bfs.test.employeeserv.api.model.Employee;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Interface for employee resource operations.
 */
public interface EmployeeResource {

    /**
     * Retrieves the {@link Employee} resource by id.
     *
     * @param id employee id.
     * @return {@link Employee} resource.
     */
    @RequestMapping("/v1/bfs/employees/{id}")
    ResponseEntity<Employee> employeeGetById(@PathVariable("id") String id)
    		throws EmployeeNotFoundException;

    @PostMapping("/v1/bfs/create-employee")
    ResponseEntity<Employee> createEmployee(@RequestBody Employee emp) 
    		throws EmployeeIdFoundException, AddressNotFoundException;
    
}
