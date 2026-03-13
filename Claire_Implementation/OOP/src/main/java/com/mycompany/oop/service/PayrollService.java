package com.mycompany.oop.service;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.PayrollRecord;
import com.mycompany.oop.model.PayrollSummary;
import com.mycompany.oop.model.PayrollHistoryRecord;
import com.mycompany.oop.repository.PayrollHistoryRepository;
import com.mycompany.oop.repository.CsvPayrollHistoryRepository;

import java.util.List;

public class PayrollService {

    private EmployeeService employeeService;
    private PayrollProcessor processor;
    private PayrollHistoryRepository historyRepository;
    private AttendanceService attendanceService;

    public PayrollService() {
        this.employeeService = new EmployeeService();
        this.processor = new PayrollProcessor();
        this.historyRepository = new CsvPayrollHistoryRepository();
        this.attendanceService = new AttendanceService();
    }

    // ================= SUMMARY (attendance-based) =================

    public PayrollSummary generatePayrollSummary(String cutoffPeriod) {

        List<Employee> employees = employeeService.getAllEmployees();

        double totalGross = 0, totalDeductions = 0, totalNet = 0;
        double totalSSS = 0, totalPhilhealth = 0, totalPagibig = 0, totalTax = 0;

        for (Employee e : employees) {

            double hours = attendanceService.getHoursForCutoff(
                    e.getEmployeeId(), cutoffPeriod);

            PayrollRecord record = processor.processPayroll(e, hours);

            totalGross += record.getGross();
            totalDeductions += record.getTotalDeductions();
            totalNet += record.getNet();
            totalSSS += record.getSss();
            totalPhilhealth += record.getPhilhealth();
            totalPagibig += record.getPagibig();
            totalTax += record.getTax();
        }

        return new PayrollSummary(
                totalGross, totalDeductions, totalNet,
                employees.size(),
                totalSSS, totalPhilhealth, totalPagibig, totalTax
        );
    }

    // ================= SUMMARY (legacy, fixed hours) =================

    public PayrollSummary generatePayrollSummary(double hoursWorked) {

        List<Employee> employees = employeeService.getAllEmployees();

        double totalGross = 0, totalDeductions = 0, totalNet = 0;
        double totalSSS = 0, totalPhilhealth = 0, totalPagibig = 0, totalTax = 0;

        for (Employee e : employees) {

            PayrollRecord record = processor.processPayroll(e, hoursWorked);

            totalGross += record.getGross();
            totalDeductions += record.getTotalDeductions();
            totalNet += record.getNet();
            totalSSS += record.getSss();
            totalPhilhealth += record.getPhilhealth();
            totalPagibig += record.getPagibig();
            totalTax += record.getTax();
        }

        return new PayrollSummary(
                totalGross, totalDeductions, totalNet,
                employees.size(),
                totalSSS, totalPhilhealth, totalPagibig, totalTax
        );
    }

    // ================= PROCESS & SAVE (attendance-based) =================

    public boolean processAndSavePayroll(String cutoffPeriod, boolean overwriteIfExists) {

        if (historyRepository.existsByCutoff(cutoffPeriod)) {
            if (!overwriteIfExists) {
                return false;
            }
            historyRepository.deleteByCutoff(cutoffPeriod);
        }

        List<Employee> employees = employeeService.getAllEmployees();

        for (Employee e : employees) {

            double hours = attendanceService.getHoursForCutoff(
                    e.getEmployeeId(), cutoffPeriod);

            PayrollRecord record = processor.processPayroll(e, hours);

            PayrollHistoryRecord history = new PayrollHistoryRecord(
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

    // ================= PROCESS & SAVE (legacy, fixed hours) =================

    public boolean processAndSavePayroll(
            double hoursWorked,
            String cutoffPeriod,
            boolean overwriteIfExists) {

        if (historyRepository.existsByCutoff(cutoffPeriod)) {
            if (!overwriteIfExists) {
                return false;
            }
            historyRepository.deleteByCutoff(cutoffPeriod);
        }

        List<Employee> employees = employeeService.getAllEmployees();

        for (Employee e : employees) {

            PayrollRecord record = processor.processPayroll(e, hoursWorked);

            PayrollHistoryRecord history = new PayrollHistoryRecord(
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

    // ================= ATTENDANCE INTEGRATION =================

    public List<String> getAvailableCutoffs() {
        return attendanceService.getAvailableCutoffs();
    }

    public double getHoursForCutoff(int employeeId, String cutoffPeriod) {
        return attendanceService.getHoursForCutoff(employeeId, cutoffPeriod);
    }

    // ================= SINGLE EMPLOYEE =================

    public PayrollRecord processPayrollForEmployee(
            Employee employee, double hoursWorked) {
        return processor.processPayroll(employee, hoursWorked);
    }

    // ================= HISTORY =================

    public List<PayrollHistoryRecord> getPayrollHistoryForEmployee(int employeeId) {
        return historyRepository.findByEmployeeId(employeeId);
    }

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
