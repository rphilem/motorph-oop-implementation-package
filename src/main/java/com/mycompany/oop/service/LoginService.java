/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.service;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.repository.EmployeeRepository;
import com.mycompany.oop.repository.CsvEmployeeRepository;

import java.util.List;

public class LoginService {

    private EmployeeRepository repository;

    // Constructor
    public LoginService() {
        this.repository = new CsvEmployeeRepository();
    }

    // Login method
    public Employee login(String username, String password) {

        List<Employee> employees = repository.getAllEmployees();

        for (Employee emp : employees) {

            if (emp.getUsername().equals(username)
                    && emp.getPassword().equals(password)) {

                return emp;  // login successful
            }
        }

        return null; // login failed
    }
}