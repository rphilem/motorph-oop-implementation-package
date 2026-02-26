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

        double gross;
        double basicHalf;
        double allowanceHalf;

        // ================= GROSS COMPUTATION =================

        if (emp.getHourlyRate() > 0) {

            gross = hoursWorked * emp.getHourlyRate();

            basicHalf = gross;
            allowanceHalf = 0;

        } else {

            basicHalf = emp.getBasicSalary() / 2;
            allowanceHalf = emp.getAllowance() / 2;

            gross = basicHalf + allowanceHalf;
        }

        record.setBasicComponent(basicHalf);
        record.setAllowanceComponent(allowanceHalf);
        record.setGross(gross);

        // ================= MONTHLY CONVERSION =================

        double monthlyEquivalent = gross * 2;

        // ================= GOVERNMENT DEDUCTIONS (MONTHLY) =================

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

/*
PAYROLL PROCESSOR – COMPUTATION ENGINE UPDATE SUMMARY

Purpose:
Handles payroll computation logic per employee
for a specific payroll cutoff period.

Core Responsibility:
• Compute cutoff-based gross pay
• Convert cutoff gross to monthly equivalent for deduction bracket evaluation
• Compute government deductions (SSS, PhilHealth, Pag-IBIG, Withholding Tax)
• Convert deductions back to cutoff values
• Return a PayrollRecord object containing full breakdown

Major Correction (Computation Consistency Fix):
• Standardized all stored deduction values as CUTOFF-based amounts
• Removed mismatch between monthly deduction display and semi-monthly totals
• Ensured:
      Net Salary = Gross (cutoff) - Total Deductions (cutoff)
• Allowance is treated consistently as monthly and divided per cutoff
• Eliminated rounding inconsistencies caused by mixed storage logic

Architectural Improvement:
• Government deduction logic fully moved to backend
• UI no longer performs any payroll computation
• PayrollRecord encapsulates breakdown data cleanly
• Employee model remains immutable and payroll-agnostic

Why This Matters:
• Prevents mismatch between displayed breakdown and computed totals
• Ensures payslip integrity
• Makes payroll logic auditable and predictable
• Enables proper approval workflow without recalculation risk

Design Principle:
Stateless computation engine.
No internal data persistence.
Pure input (Employee + hoursWorked) → deterministic PayrollRecord output.

Scalability Roadmap:
• Replace simplified SSS logic with official contribution table
• Externalize tax brackets to configuration file
• Dynamic PhilHealth rate update support
• Per-employee attendance-based computation
• Future payroll policy versioning system
*/