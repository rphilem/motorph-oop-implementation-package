/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.service;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.Leave;
import com.mycompany.oop.model.DashboardSummary;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardService {

    private EmployeeService employeeService;
    private LeaveService leaveService;
    private PayrollProcessor processor;

    public DashboardService() {
        this.employeeService = new EmployeeService();
        this.leaveService = new LeaveService();
        this.processor = new PayrollProcessor();
    }

    public DashboardSummary generateSummary() {

        List<Employee> employees = employeeService.getAllEmployees();
        List<Leave> leaves = leaveService.getAllLeaves();

        int totalEmployees = employees.size();

        int activeEmployees = (int) employees.stream()
                .filter(e -> !e.getEmploymentStatus()
                        .equalsIgnoreCase("Inactive"))
                .count();

        int pendingLeaves = (int) leaves.stream()
                .filter(l -> l.getStatus().equalsIgnoreCase("PENDING"))
                .count();

        Map<String, Long> roleCounts = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getRole,
                        Collectors.counting()));

        double totalGross = employees.stream()
                .mapToDouble(Employee::getGrossSalary).sum();

        double totalNet = employees.stream()
                .mapToDouble(Employee::getNetSalary).sum();

        double totalDeductions = employees.stream()
                .mapToDouble(Employee::getDeductions).sum();

        double totalAllowance = employees.stream()
                .mapToDouble(Employee::getTotalAllowance).sum();

        return new DashboardSummary(
                totalEmployees,
                activeEmployees,
                pendingLeaves,
                roleCounts.getOrDefault("Admin", 0L).intValue(),
                roleCounts.getOrDefault("HR", 0L).intValue(),
                roleCounts.getOrDefault("Finance", 0L).intValue(),
                roleCounts.getOrDefault("Employee", 0L).intValue(),
                roleCounts.getOrDefault("IT", 0L).intValue(),
                totalGross,
                totalNet,
                totalDeductions,
                totalAllowance
        );
    }
}

