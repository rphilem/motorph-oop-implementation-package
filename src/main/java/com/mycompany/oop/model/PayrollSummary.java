/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.model;

public class PayrollSummary {

    private double totalGross;
    private double totalDeductions;
    private double totalNet;
    private int employeeCount;

    public PayrollSummary(double totalGross,
                          double totalDeductions,
                          double totalNet,
                          int employeeCount) {

        this.totalGross = totalGross;
        this.totalDeductions = totalDeductions;
        this.totalNet = totalNet;
        this.employeeCount = employeeCount;
    }

    public double getTotalGross() {
        return totalGross;
    }

    public double getTotalDeductions() {
        return totalDeductions;
    }

    public double getTotalNet() {
        return totalNet;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }
}