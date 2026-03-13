package com.mycompany.oop.service;

import com.mycompany.oop.model.Employee;

public interface HROperations {

    void addEmployeeRecord(Employee employee);

    void updateEmployeeRecord(Employee employee);

    void deleteEmployeeRecord(String employeeId);
}
