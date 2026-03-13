package com.mycompany.oop.service;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.repository.EmployeeRepository;
import com.mycompany.oop.repository.CsvEmployeeRepository;

public class Authentication {

    private EmployeeRepository repository;

    public Authentication() {
        this.repository = new CsvEmployeeRepository();
    }

    public Employee getEmployeeData(String empId) {

        int id = Integer.parseInt(empId);

        return repository.getAllEmployees().stream()
                .filter(emp -> emp.getEmployeeId() == id)
                .findFirst()
                .orElse(null);
    }
}
