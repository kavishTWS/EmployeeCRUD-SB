package com.CRUD.Employee.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;

import com.CRUD.Employee.entity.Employee;
import com.CRUD.Employee.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void shouldReturnAllEmployees() {

        List<Employee> mockEmployees = List.of(
                new Employee(1L, "John Doe", 30, 75000.0),
                new Employee(2L, "Jane Smith", 40, 85000.0)
        );

        when(employeeRepository.findAll()).thenReturn(mockEmployees);

        List<Employee> result = employeeService.listEmployee();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Smith", result.get(1).getName());

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void shouldCreateEmployee() {

        Employee inputEmployee = new Employee(null, "kavish", 21, 75000.0); // Input ID is null for creation
        Employee savedEmployee = new Employee(1L, "kavish", 21, 75000.0);  // Expected response with generated ID

        when(employeeRepository.save(inputEmployee)).thenReturn(savedEmployee);

        Employee result = employeeService.createEmployee(inputEmployee);

        assertNotNull(result);
        assertEquals(savedEmployee.getId(), result.getId());
        assertEquals(savedEmployee.getName(), result.getName());
        assertEquals(savedEmployee.getAge(), result.getAge());
        assertEquals(savedEmployee.getSalary(), result.getSalary());
        VerificationMode times;
        verify(employeeRepository, times(1)).save(inputEmployee);

    }

    @SuppressWarnings("null")
    @Test
    void shouldReturnEmployeeWhenExists() {

        Long employeeId = 1L;
        Employee employee = new Employee(employeeId, "John Doe", 30, 50000.0);
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        ResponseEntity<Employee> response = employeeService.getEmployeeId(employeeId);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode()); // HTTP 200
        assertNotNull(response.getBody());
        assertEquals(employeeId, response.getBody().getId());
        assertEquals(employee.getName(), response.getBody().getName());
        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    void shouldReturnNotFoundWhenEmployeeDoesNotExist() {

        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        ResponseEntity<Employee> response = employeeService.getEmployeeId(employeeId);

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode()); // HTTP 404
        assertNull(response.getBody());
        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @SuppressWarnings("null")
    @Test
    void shouldUpdateEmployeeWhenExists() {

        Long employeeId = 1L;
        Employee existingEmployee = new Employee(employeeId, "John Doe", 30, 50000.0);
        Employee updatedEmployee = new Employee(null, "Jane Doe", 35, 60000.0);
        Employee savedEmployee = new Employee(employeeId, "Jane Doe", 35, 60000.0);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        ResponseEntity<Employee> response = employeeService.updateEmployee(employeeId, updatedEmployee);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode()); // HTTP 200
        assertNotNull(response.getBody());
        assertEquals(savedEmployee.getId(), response.getBody().getId());
        assertEquals(savedEmployee.getName(), response.getBody().getName());
        assertEquals(savedEmployee.getAge(), response.getBody().getAge());
        assertEquals(savedEmployee.getSalary(), response.getBody().getSalary());

        // Verify that the repository methods were called
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void shouldReturnNotFoundWhenEmployeeDoesNotExistForUpdate() {

        Long employeeId = 1L;
        Employee updatedEmployee = new Employee(null, "Jane Doe", 35, 60000.0);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        ResponseEntity<Employee> response = employeeService.updateEmployee(employeeId, updatedEmployee);

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        // Verify that the repository's save method was not called
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void shouldDeleteEmployeeWhenExists() {

        Long employeeId = 1L;
        Employee existingEmployee = new Employee(employeeId, "John Doe", 30, 50000.0);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));

        ResponseEntity<Object> response = employeeService.deleteEmployee(employeeId);

        assertNotNull(response);
        assertEquals(NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        // Verify that the repository's delete method was called
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).delete(existingEmployee);
    }

    @Test
    void shouldReturnNotFoundWhenEmployeeDoesNotExistForDelete() {

        Long employeeId = 1L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());
        ResponseEntity<Object> response = employeeService.deleteEmployee(employeeId);

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        // Verify that the repository's delete method was never called
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, never()).delete(any(Employee.class));
    }

}
