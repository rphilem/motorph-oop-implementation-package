/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.service;

import java.util.List;
import com.mycompany.oop.model.Employee;
import com.mycompany.oop.repository.EmployeeRepository;
import com.mycompany.oop.repository.CsvEmployeeRepository;
import java.util.Map;

public class EmployeeService {

    private EmployeeRepository repository;

    public EmployeeService() {
        this.repository = new CsvEmployeeRepository();
    }

    public List<Employee> getAllEmployees() {
        return repository.getAllEmployees();
    }

    public Employee findById(int id) {
        return repository.findEmployee(id);
    }

    public void addEmployee(Employee employee) {
        repository.addEmployee(employee);
    }

    public void updateEmployee(Employee employee) {
        repository.updateEmployee(employee);
    }

    public void deleteEmployee(int id) {
        repository.deleteEmployee(id);
    }
    
    public java.util.Map<String, Integer> getUserRoleCounts() {

        java.util.List<Employee> list = repository.getAllEmployees();
        java.util.Map<String, Integer> roleCounts = new java.util.HashMap<>();

        for (Employee e : list) {
            String role = e.getRole();
            roleCounts.put(role, roleCounts.getOrDefault(role, 0) + 1);
        }

        return roleCounts;
    }
    
    public int getTotalUsers() {
        return repository.getAllEmployees().size();
    }

    public void changeRole(int id, String newRole) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void resetPassword(int id, String newPass) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}