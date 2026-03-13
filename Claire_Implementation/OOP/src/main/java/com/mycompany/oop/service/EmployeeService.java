package com.mycompany.oop.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.mycompany.oop.model.Employee;
import com.mycompany.oop.repository.EmployeeRepository;
import com.mycompany.oop.repository.CsvEmployeeRepository;

public class EmployeeService implements HROperations, EmployeeOperations {

    private EmployeeRepository repository;

    public EmployeeService() {
        this.repository = new CsvEmployeeRepository();
    }

    // ================= EmployeeOperations =================

    @Override
    public String viewEmployeeRecords() {
        List<Employee> employees = repository.getAllEmployees();
        StringBuilder sb = new StringBuilder();
        for (Employee emp : employees) {
            sb.append(emp.getEmployeeNumber()).append(" - ")
              .append(emp.getFirstName()).append(" ")
              .append(emp.getLastName()).append(" | ")
              .append(emp.getPosition()).append("\n");
        }
        return sb.toString();
    }

    // ================= HROperations =================

    @Override
    public void addEmployeeRecord(Employee employee) {
        repository.addEmployee(employee);
    }

    @Override
    public void updateEmployeeRecord(Employee employee) {
        repository.updateEmployee(employee);
    }

    @Override
    public void deleteEmployeeRecord(String employeeId) {
        int id = Integer.parseInt(employeeId);
        repository.deleteEmployee(id);
    }

    // ================= QUERY METHODS =================

    public List<Employee> getAllEmployees() {
        return repository.getAllEmployees();
    }

    public Employee findById(int id) {
        return repository.findEmployee(id);
    }

    public Map<String, Integer> getUserRoleCounts() {
        return repository.getAllEmployees().stream()
                .collect(Collectors.groupingBy(
                        Employee::getRole,
                        Collectors.collectingAndThen(
                                Collectors.counting(),
                                Long::intValue)));
    }

    public int getTotalUsers() {
        return repository.getAllEmployees().size();
    }

    public void changeRole(int id, String newRole) {
        Employee employee = repository.findEmployee(id);
        if (employee != null) {
            employee.setRole(newRole);
            repository.updateEmployee(employee);
        }
    }

    public void resetPassword(int id, String newPass) {
        Employee employee = repository.findEmployee(id);
        if (employee != null) {
            employee.setPassword(newPass);
            repository.updateEmployee(employee);
        }
    }
}
