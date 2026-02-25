/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.service;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.PayrollSummary;

import java.util.List;

public class PayrollService {

    private EmployeeService employeeService;
    private PayrollProcessor processor;

    public PayrollService() {
        this.employeeService = new EmployeeService();
        this.processor = new PayrollProcessor();
    }

    public PayrollSummary generatePayrollSummary() {

        List<Employee> list = employeeService.getAllEmployees();

        double grossTotal = 0;
        double deductionTotal = 0;
        double netTotal = 0;

        for (Employee e : list) {

            grossTotal += processor.computeGross(e);
            deductionTotal += processor.computeDeductions(e);
            netTotal += processor.computeNet(e);
        }

        return new PayrollSummary(
                grossTotal,
                deductionTotal,
                netTotal,
                list.size()
        );
    }

    public List<Employee> getEmployees() {
        return employeeService.getAllEmployees();
    }
}
