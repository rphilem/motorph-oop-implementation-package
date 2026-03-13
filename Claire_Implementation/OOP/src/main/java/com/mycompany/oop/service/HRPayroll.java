package com.mycompany.oop.service;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.PayrollRecord;

public class HRPayroll {

    private PayrollProcessor processor;

    public HRPayroll() {
        this.processor = new PayrollProcessor();
    }

    public double calculateNetPay(Employee employee, double hoursWorked) {

        PayrollRecord record = processor.processPayroll(employee, hoursWorked);
        return record.getNet();
    }
}
