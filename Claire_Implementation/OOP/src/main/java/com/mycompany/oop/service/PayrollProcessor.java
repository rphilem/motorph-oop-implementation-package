package com.mycompany.oop.service;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.PayrollRecord;

public class PayrollProcessor {

    public PayrollRecord processPayroll(Employee emp, double hoursWorked) {

        PayrollRecord record = new PayrollRecord(emp, hoursWorked);

        double basicHalf;
        double allowanceHalf = emp.getTotalAllowance() / 2;

        // ================= GROSS COMPUTATION =================

        if (emp.getHourlyRate() > 0) {

            basicHalf = hoursWorked * emp.getHourlyRate();

        } else {

            basicHalf = emp.getBasicSalary() / 2;
        }

        double gross = basicHalf + allowanceHalf;

        record.setBasicComponent(basicHalf);
        record.setAllowanceComponent(allowanceHalf);
        record.setGross(gross);

        // ================= MONTHLY CONVERSION =================

        double monthlyEquivalent = gross * 2;

        // ================= SSS (2024 Table) =================
        // Employee share: 4.5% of Monthly Salary Credit
        // Maximum MSC: 30,000

        double sss = Math.min(monthlyEquivalent, 30000) * 0.045;

        // ================= PHILHEALTH =================
        // 2024 rate: 5% total, employee share = 2.5%
        // Floor: 10,000 monthly salary, Ceiling: 100,000

        double philhealthBase = Math.max(10000,
                Math.min(monthlyEquivalent, 100000));
        double philhealth = philhealthBase * 0.025;

        // ================= PAG-IBIG =================
        // Employee: 2% if salary > 1,500, else 1%
        // Maximum contribution: 200 (employee share, as of 2024)

        double pagibig;
        if (monthlyEquivalent <= 1500) {
            pagibig = monthlyEquivalent * 0.01;
        } else {
            pagibig = monthlyEquivalent * 0.02;
        }
        pagibig = Math.min(pagibig, 200);

        // ================= WITHHOLDING TAX (2023 TRAIN Law) =================

        double tax;
        if (monthlyEquivalent <= 20832) tax = 0;
        else if (monthlyEquivalent < 33333)
            tax = (monthlyEquivalent - 20833) * 0.20;
        else if (monthlyEquivalent < 66667)
            tax = 2500 + (monthlyEquivalent - 33333) * 0.25;
        else if (monthlyEquivalent < 166667)
            tax = 10833 + (monthlyEquivalent - 66667) * 0.30;
        else if (monthlyEquivalent < 666667)
            tax = 40833.33 + (monthlyEquivalent - 166667) * 0.32;
        else
            tax = 200833.33 + (monthlyEquivalent - 666667) * 0.35;

        // ================= CONVERT TO SEMI-MONTHLY =================

        double cutoffSss = sss / 2;
        double cutoffPhilhealth = philhealth / 2;
        double cutoffPagibig = pagibig / 2;
        double cutoffTax = tax / 2;

        double totalDeductions =
                cutoffSss +
                cutoffPhilhealth +
                cutoffPagibig +
                cutoffTax;

        record.setSss(cutoffSss);
        record.setPhilhealth(cutoffPhilhealth);
        record.setPagibig(cutoffPagibig);
        record.setTax(cutoffTax);
        record.setTotalDeductions(totalDeductions);

        record.setNet(gross - totalDeductions);

        return record;
    }
}
