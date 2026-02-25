/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.service;

import com.mycompany.oop.model.Employee;

public class PayrollProcessor {

    public double computeGross(Employee employee) {
        return employee.computeGrossSalary();
    }

    public double computeDeductions(Employee employee) {
        return employee.computeDeductions();
    }

    public double computeNet(Employee employee) {
        return employee.computeNetSalary();
    }
}