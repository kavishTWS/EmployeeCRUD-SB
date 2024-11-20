package com.CRUD.Employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.CRUD.Employee.entity.Employee;
import com.CRUD.Employee.repository.EmployeeRepository;

@Service
public class EmployeeService {

@Autowired
private EmployeeRepository employeeRepository;

public Employee createEmployee(Employee employee){
  return employeeRepository.save(employee);
}

public List<Employee> listEmployee(){
    return employeeRepository.findAll();
}

public ResponseEntity<Employee> getEmployeeId(Long id){
    return employeeRepository.findById(id)
    .map(ResponseEntity::ok)
    .orElse(ResponseEntity.notFound().build());

}

public ResponseEntity<Employee> updateEmployee(Long id, Employee updatedEmployee){
    return employeeRepository.findById(id)
    .map(employee ->{
        if(updatedEmployee.getName() != null){
            employee.setName(updatedEmployee.getName());
        }
       
        if (updatedEmployee.getAge() != null) {
            employee.setAge(updatedEmployee.getAge());
        }
        if (updatedEmployee.getSalary() != null) {
            employee.setSalary(updatedEmployee.getSalary());
        }
        employeeRepository.save(employee);
        return ResponseEntity.ok(employee);
 })
    .orElse(ResponseEntity.notFound().build()); 
}

public ResponseEntity<Object> deleteEmployee(Long id){
    return employeeRepository.findById(id)
    .map(employee->{
        employeeRepository.delete(employee);
        return ResponseEntity.noContent().build();
    })
    .orElse(ResponseEntity.notFound().build());

}


}
