package com.CRUD.Employee.controller;

import static javax.swing.UIManager.get;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.CRUD.Employee.entity.Employee;
import com.CRUD.Employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = EmployeeController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @Test
    void shouldReturnEmptyList() throws Exception {
        mockMvc.perform((RequestBuilder) get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]")); // Expecting an empty list
    }

    @Test
    void shouldCreateEmployee() throws Exception {
    // Input data
    Employee inputEmployee = new Employee(null, "kavish", 21, 75000.0); // Input ID is null for creation
    Employee outputEmployee = new Employee(1L, "kavish", 21, 75000.0);  // Expected response with generated ID

    // Mock service behavior
    when(employeeService.createEmployee(any(Employee.class))).thenReturn(outputEmployee);

    // Perform POST request
    mockMvc.perform(post("/api/employees")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(inputEmployee))) // Serialize input to JSON
            .andExpect(status().isOk()) // Expect HTTP 200
            .andExpect(content().json(objectMapper.writeValueAsString(outputEmployee))); // Compare JSON response


    }

    @Test
    void shouldReturnEmployeeById() throws Exception{

    Long employeeId = 1L;
    Employee mockEmployee = new Employee(employeeId, "kavish", 21, 35000.0);

    ResponseEntity<Employee> mockResponseEntity = ResponseEntity.ok(mockEmployee);

    when(employeeService.getEmployeeId(employeeId)).thenReturn(mockResponseEntity);

    mockMvc.perform( get("/api/employees/{id}", employeeId)
    .contentType("application/json"))
    .andExpect(status().isOk())
    .andExpect(content().json( objectMapper.writeValueAsString( mockEmployee)));

    }

    @Test
    void shoudUpdateEmployee() throws Exception{

        Long employeeId = 1L;
        Employee updateEmployee = new Employee(employeeId, "Ram", 22, 45000.0);
        

        ResponseEntity<Employee> mockResponseEntity = ResponseEntity.ok(updateEmployee);

        when( employeeService.updateEmployee(eq(employeeId), any(Employee.class))).thenReturn(mockResponseEntity);

        mockMvc.perform(put("/api/employees/{id}", employeeId)
        .contentType("application/json") // Set Content-Type to JSON
        .content(objectMapper.writeValueAsString(updateEmployee))) // Serialize input to JSON
        .andExpect(status().isOk()) // Expect HTTP 200
        .andExpect(content().json(objectMapper.writeValueAsString(updateEmployee))); // Validate JSON response          
        
    }

    @Test
    void shouldDeleteEmployee() throws Exception {
    // Mock data
    Long employeeId = 1L;
    ResponseEntity<Object> mockResponseEntity = ResponseEntity.ok().build(); // Simulate 200 OK with no body

    // Mock service behavior
    when(employeeService.deleteEmployee(employeeId)).thenReturn(mockResponseEntity);

    // Perform DELETE request
    mockMvc.perform(delete("/api/employees/{id}", employeeId)
            .contentType("application/json")) // Set Content-Type to JSON
            .andExpect(status().isOk()); // Expect HTTP 200
}


}
