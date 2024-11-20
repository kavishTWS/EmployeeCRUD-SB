package com.CRUD.Employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CRUD.Employee.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}