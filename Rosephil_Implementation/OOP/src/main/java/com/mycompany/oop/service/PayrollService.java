/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.service;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.PayrollRecord;
import com.mycompany.oop.model.PayrollSummary;
import com.mycompany.oop.model.PayrollHistoryRecord;
import com.mycompany.oop.repository.PayrollHistoryRepository;
import com.mycompany.oop.repository.CsvPayrollHistoryRepository;

import java.util.List;

/*
    PayrollService

    Business layer for payroll processing.
    Coordinates:
    - EmployeeService
    - PayrollProcessor
    - PayrollHistoryRepository
*/

public class PayrollService {

    private EmployeeService employeeService;
    private PayrollProcessor processor;
    private PayrollHistoryRepository historyRepository;

    public PayrollService() {
        this.employeeService = new EmployeeService();
        this.processor = new PayrollProcessor();
        this.historyRepository = new CsvPayrollHistoryRepository();
    }

    /*
        Generate payroll summary for dashboard cards
    */
    public PayrollSummary generatePayrollSummary(double hoursWorked) {

        List<Employee> employees =
                employeeService.getAllEmployees();

        double totalGross = 0;
        double totalDeductions = 0;
        double totalNet = 0;

        double totalSSS = 0;
        double totalPhilhealth = 0;
        double totalPagibig = 0;
        double totalTax = 0;

        for (Employee e : employees) {

            PayrollRecord record =
                    processor.processPayroll(e, hoursWorked);

            totalGross += record.getGross();
            totalDeductions += record.getTotalDeductions();
            totalNet += record.getNet();

            totalSSS += record.getSss();
            totalPhilhealth += record.getPhilhealth();
            totalPagibig += record.getPagibig();
            totalTax += record.getTax();
        }

        return new PayrollSummary(
                totalGross,
                totalDeductions,
                totalNet,
                employees.size(),
                totalSSS,
                totalPhilhealth,
                totalPagibig,
                totalTax
        );
    }

    /*
        Process payroll and save history
        Prevent duplicate cutoff processing
    */
    public boolean processAndSavePayroll(
            double hoursWorked,
            String cutoffPeriod,
            boolean overwriteIfExists
    ) {

        // Check if cutoff already processed
        if (historyRepository.existsByCutoff(cutoffPeriod)) {

            if (!overwriteIfExists) {
                return false; // duplicate not allowed
            }

            // Overwrite mode for demo reset
            historyRepository.deleteByCutoff(cutoffPeriod);
        }

        List<Employee> employees =
                employeeService.getAllEmployees();

        for (Employee e : employees) {

            PayrollRecord record =
                    processor.processPayroll(e, hoursWorked);

            PayrollHistoryRecord history =
                new PayrollHistoryRecord(
                    e.getEmployeeId(),
                    cutoffPeriod,
                    record.getBasicComponent(),
                    record.getAllowanceComponent(),
                    record.getGross(),
                    record.getSss(),
                    record.getPhilhealth(),
                    record.getPagibig(),
                    record.getTax(),
                    record.getTotalDeductions(),
                    record.getNet()
                );

            historyRepository.savePayrollRecord(history);
        }

        return true;
    }

    /*
        Get payroll history of one employee
        Used by PayslipPanel
    */
    public List<PayrollHistoryRecord>
    getPayrollHistoryForEmployee(int employeeId) {

        return historyRepository
                .findByEmployeeId(employeeId);
    }

    /*
        Expose employees for payroll table
    */
    public List<Employee> getEmployees() {
        return employeeService.getAllEmployees();
    }
    
    public List<PayrollHistoryRecord> getAllPayrollHistory() {
        return historyRepository.findAll();
    }
    
    public void deleteCutoff(String cutoffPeriod) {
        historyRepository.deleteByCutoff(cutoffPeriod);
    }
    
}

/*
PAYROLL SERVICE – BUSINESS LOGIC UPDATE SUMMARY

Purpose:
Central business layer for payroll operations.

Responsibilities:
Generate payroll summaries for dashboard
Process payroll for all employees
Save payroll history
Prevent duplicate cutoff processing
Provide employee-specific payroll history

Major Enhancements:
1. Duplicate Cutoff Protection

processAndSavePayroll(...) now:
Checks if cutoff already exists
Blocks duplicate runs
Allows optional overwrite for demo

2. Government Deduction Aggregation

generatePayrollSummary(...) now calculates:
Total Gross
Total Deductions
Total Net
Total SSS
Total PhilHealth
Total Pag-IBIG
Total Tax

3. Payslip Integration
getPayrollHistoryForEmployee(...)
Enables employee-specific payroll history display.

Architecture Improvements:
Separated payroll processing (PayrollProcessor)
Separated data persistence (Repository)
Service coordinates both layers

Scalability:
Easily extendable to:
Attendance integration
Per-employee hours
Multi-cutoff automation
Payroll approval workflows
*/
