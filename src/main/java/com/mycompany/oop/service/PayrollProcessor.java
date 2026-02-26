/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.service;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.PayrollRecord;

public class PayrollProcessor {

    public PayrollRecord processPayroll(Employee emp, double hoursWorked) {

        PayrollRecord record = new PayrollRecord(emp, hoursWorked);

        // 1️⃣ Compute Gross (cutoff-based)

        double gross;

        if (emp.getHourlyRate() > 0) {
            gross = hoursWorked * emp.getHourlyRate();
        } else {
            // Assume both basicSalary and allowance are MONTHLY
            gross = (emp.getBasicSalary() / 2) +
                    (emp.getAllowance() / 2);
        }

        record.setGross(gross);

        // Convert to monthly for deduction computation only
        double monthlyEquivalent = gross * 2;

        // 2️⃣ Compute MONTHLY deductions

        double sss;
        if (monthlyEquivalent <= 3250) sss = 135;
        else if (monthlyEquivalent <= 3749.99) sss = 157.5;
        else if (monthlyEquivalent <= 4249.99) sss = 180;
        else if (monthlyEquivalent <= 4749.99) sss = 202.5;
        else if (monthlyEquivalent <= 5249.99) sss = 225;
        else sss = 247.5;

        double philhealth = Math.max(300,
                Math.min(monthlyEquivalent * 0.03, 1800));

        double pagibig =
                (monthlyEquivalent <= 1500)
                        ? monthlyEquivalent * 0.01
                        : monthlyEquivalent * 0.02;

        pagibig = Math.min(pagibig, 100);

        double tax;
        if (monthlyEquivalent <= 20832) tax = 0;
        else if (monthlyEquivalent < 33333)
            tax = (monthlyEquivalent - 20833) * 0.2;
        else if (monthlyEquivalent < 66667)
            tax = 2500 + (monthlyEquivalent - 33333) * 0.25;
        else if (monthlyEquivalent < 166667)
            tax = 10833 + (monthlyEquivalent - 66667) * 0.3;
        else
            tax = 40833.33 + (monthlyEquivalent - 166667) * 0.32;

        // 3️⃣ Convert everything to cutoff amounts

        double cutoffSss = sss / 2;
        double cutoffPhilhealth = philhealth / 2;
        double cutoffPagibig = pagibig / 2;
        double cutoffTax = tax / 2;

        double totalDeductions =
                cutoffSss +
                cutoffPhilhealth +
                cutoffPagibig +
                cutoffTax;

        // 4️⃣ Store cutoff values only

        record.setSss(cutoffSss);
        record.setPhilhealth(cutoffPhilhealth);
        record.setPagibig(cutoffPagibig);
        record.setTax(cutoffTax);
        record.setTotalDeductions(totalDeductions);

        record.setNet(gross - totalDeductions);

        return record;
    }
}

/*
PAYROLL PROCESSOR – COMPUTATION ENGINE UPDATE SUMMARY

Purpose:
Handles payroll computation logic per employee.

Responsibilities:
Compute gross pay
Compute SSS, PhilHealth, Pag-IBIG, Tax
Return PayrollRecord object containing breakdown

Enhancements:
Converted government deductions from UI logic
into backend processor logic
Structured return as PayrollRecord object
instead of mutating Employee object
Clean separation of computation and persistence

Why This Matters:
Keeps Employee model clean
Makes payroll computation testable
Allows easy policy changes (tax updates)

Scalability:
Future upgrade paths:
Official SSS table integration
Tax table updates
Configurable deduction rates
Attendance-based hourly calculation

Design Principle:
Stateless processing component.
No data storage inside processor.
*/