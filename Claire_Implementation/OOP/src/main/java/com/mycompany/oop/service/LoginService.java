/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.service;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.repository.EmployeeRepository;
import com.mycompany.oop.repository.CsvEmployeeRepository;

import java.util.Optional;

public class LoginService {

    private EmployeeRepository repository;

    public LoginService() {
        this.repository = new CsvEmployeeRepository();
    }

    public Employee login(String username, String password) {

        Optional<Employee> match = repository.getAllEmployees()
                .stream()
                .filter(emp -> emp.getUsername().equals(username)
                        && emp.getPassword().equals(password))
                .findFirst();

        return match.orElse(null);
    }
}