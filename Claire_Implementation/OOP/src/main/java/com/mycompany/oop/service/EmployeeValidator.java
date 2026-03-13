package com.mycompany.oop.service;

import com.mycompany.oop.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeValidator {

    public List<String> validate(Employee employee, List<Employee> existingEmployees, boolean isNew) {

        List<String> errors = new ArrayList<>();

        // Required fields
        if (employee.getFirstName() == null || employee.getFirstName().isEmpty())
            errors.add("First Name is required.");
        if (employee.getLastName() == null || employee.getLastName().isEmpty())
            errors.add("Last Name is required.");
        if (employee.getPosition() == null || employee.getPosition().isEmpty())
            errors.add("Position is required.");
        if (employee.getEmploymentStatus() == null || employee.getEmploymentStatus().isEmpty())
            errors.add("Employment Status is required.");
        if (employee.getBirthday() == null || employee.getBirthday().isEmpty())
            errors.add("Birthday is required.");
        if (employee.getAddress() == null || employee.getAddress().isEmpty())
            errors.add("Address is required.");
        if (employee.getPhoneNumber() == null || employee.getPhoneNumber().isEmpty())
            errors.add("Phone Number is required.");

        // Email format
        if (employee.getEmail() == null || employee.getEmail().isEmpty()) {
            errors.add("Email is required.");
        } else if (!employee.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            errors.add("Email format is invalid.");
        }

        // Numeric checks (salary/allowances)
        if (employee.getBasicSalary() < 0)
            errors.add("Basic Salary must be a positive number.");
        if (employee.getRiceSubsidy() < 0)
            errors.add("Rice Subsidy must be a positive number.");
        if (employee.getPhoneAllowance() < 0)
            errors.add("Phone Allowance must be a positive number.");
        if (employee.getClothingAllowance() < 0)
            errors.add("Clothing Allowance must be a positive number.");
        if (employee.getHourlyRate() < 0)
            errors.add("Hourly Rate must be a positive number.");

        // Employee ID duplicate check (only for new employees)
        if (isNew) {
            boolean duplicate = existingEmployees.stream()
                    .anyMatch(e -> e.getEmployeeId() == employee.getEmployeeId());
            if (duplicate) {
                errors.add("Employee Number " + employee.getEmployeeId() + " already exists.");
            }
        }

        // Government ID format validations
        validateSss(employee.getSssNumber(), errors);
        validatePhilhealth(employee.getPhilhealthNumber(), errors);
        validateTin(employee.getTinNumber(), errors);
        validatePagibig(employee.getPagibigNumber(), errors);

        return errors;
    }

    // SSS format: XX-XXXXXXX-X (2 digits, dash, 7 digits, dash, 1 digit)
    private void validateSss(String sss, List<String> errors) {
        if (sss == null || sss.isEmpty()) {
            errors.add("SSS # is required.");
        } else if (!sss.matches("\\d{2}-\\d{7}-\\d")) {
            errors.add("SSS # format invalid. Expected: XX-XXXXXXX-X");
        }
    }

    // PhilHealth format: 12-digit number
    private void validatePhilhealth(String ph, List<String> errors) {
        if (ph == null || ph.isEmpty()) {
            errors.add("PhilHealth # is required.");
        } else if (!ph.matches("\\d{12}")) {
            errors.add("PhilHealth # format invalid. Expected: 12 digits.");
        }
    }

    // TIN format: XXX-XXX-XXX-XXX (3 digits, dash, 3 digits, dash, 3 digits, dash, 3 digits)
    private void validateTin(String tin, List<String> errors) {
        if (tin == null || tin.isEmpty()) {
            errors.add("TIN # is required.");
        } else if (!tin.matches("\\d{3}-\\d{3}-\\d{3}-\\d{3}")) {
            errors.add("TIN # format invalid. Expected: XXX-XXX-XXX-XXX");
        }
    }

    // Pag-IBIG format: 12-digit number
    private void validatePagibig(String pagibig, List<String> errors) {
        if (pagibig == null || pagibig.isEmpty()) {
            errors.add("Pag-IBIG # is required.");
        } else if (!pagibig.matches("\\d{12}")) {
            errors.add("Pag-IBIG # format invalid. Expected: 12 digits.");
        }
    }
}
