# MotorPH Payroll System  
MO-IT110 – Object-Oriented Programming  
Milestone 2: OOP Refactored Implementation  

---

## Project Overview

The MotorPH Payroll System is a Java-based payroll management application developed for MO-IT110 (Object-Oriented Programming).

This system was reconstructed from a procedural CP2 version and refactored into a structured Object-Oriented implementation based strictly on the approved Milestone 1 design documentation.

Milestone 2 focused on:

- Extracting business logic from the GUI
- Implementing a layered architecture
- Applying service and repository abstraction
- Enforcing separation of concerns
- Improving maintainability and scalability
- Ensuring alignment with the approved MS1 scope

This repository contains the finalized and fully tested OOP implementation.

---

## System Architecture

The application follows a layered architecture:

View Layer → Service Layer → Processor → Repository Layer → Model

### 1. View Layer
Responsible for user interaction and display only.

Includes:
- PayrollPanel
- PayslipPanel
- DashboardPanel
- Login UI
- Leave UI

The UI contains no business logic.

---

### 2. Service Layer
Handles application coordination and business rule enforcement.

Classes:
- PayrollService
- EmployeeService
- LeaveService
- LoginService
- DashboardService

Responsibilities:
- Coordinate operations
- Call the processor for payroll computation
- Interact with repository for persistence
- Enforce business validation rules

---

### 3. Processor Layer
Class:
- PayrollProcessor

Responsibilities:
- Handles all payroll computation logic
- Stateless design
- Computes:
  - Gross Pay
  - Government Deductions (SSS, PhilHealth, Pag-IBIG, Tax)
  - Monthly-to-semi-monthly conversion
  - Net Pay

No UI or file operations exist in this layer.

---

### 4. Repository Layer
Interface:
- PayrollHistoryRepository

Implementation:
- CsvPayrollHistoryRepository

Responsibilities:
- Save payroll records
- Retrieve payroll history
- Prevent duplicate cutoff entries
- Abstract file persistence from business logic

---

### 5. Model Classes

- Employee
- PayrollRecord
- PayrollHistoryRecord
- PayrollSummary
- LeaveRequest

These classes encapsulate structured system data.

---

## Payroll Computation Logic

Payroll follows structured cutoff computation:

1. Gross Pay (Cutoff)
   Gross = Basic (Semi-Monthly) + Allowance (Semi-Monthly)

2. Monthly Equivalent
   Monthly Equivalent = Gross × 2

3. Monthly Deductions
   - SSS
   - PhilHealth
   - Pag-IBIG
   - Withholding Tax

4. Cutoff Deductions
   Cutoff Deduction = Monthly Deduction ÷ 2

5. Net Pay
   Net = Gross − Total Cutoff Deductions

Allowance is not included in government deduction computation.

All payroll formulas are centralized in PayrollProcessor.

---

## CSV Structure

Payroll records are stored in CSV format using the following structure:

employeeId,  
cutoffPeriod,  
basicComponent,  
allowanceComponent,  
gross,  
sss,  
philhealth,  
pagibig,  
tax,  
totalDeductions,  
net  

All file operations are handled through the repository layer.

---

## Testing and Validation

Testing was conducted progressively across development stages.

### Week 7 – Backend Console Testing
- Verified gross computation
- Verified deduction formulas
- Verified net salary accuracy

### Week 8 – Subclass and Polymorphism Testing
- Validated overridden methods
- Tested polymorphic employee behavior
- Confirmed dynamic binding correctness

### Week 9 – GUI Integration Testing
- Verified UI to Service communication
- Checked parameter mapping
- Conducted smoke testing
- Documented known issues

### Week 10 – Final Internal QA
- Revalidated payroll outputs
- Fixed GUI integration issues
- Confirmed layered architecture integrity
- Updated Smoke Test Checklist
- Finalized Known Issues List

The system produces consistent and correct payroll outputs.

---

## OOP Principles Applied

- Encapsulation
- Abstraction
- Polymorphism
- Separation of Concerns
- Single Responsibility Principle
- Repository Pattern
- Layered Architecture

All CP2 procedural logic was refactored into proper OOP structure.

---

## Features

- Employee Management (Add, Update, Delete)
- Payroll Processing
- Payroll History Tracking
- Duplicate Cutoff Prevention
- Payslip Viewing
- Leave Submission
- Leave Approval / Rejection
- Dashboard Metrics
- Authentication

No additional features were added beyond MS1 scope.

---

## Milestone 2 Documentation

- Refactoring Plan
- Smoke Test Checklist
- Known Issues List
- Task Breakdown
- CP2 to OOP Mapping

Full worksheet available here:

[MotorPH MS2 Worksheet (Google Drive)](https://docs.google.com/spreadsheets/d/1bFmfW2W_UjRVoHvczkLe9xB5eGNM-tLUwssmuRMDcQo/edit?usp=sharing)

---

## Group Information

Group 24  
Section S2101  

Members:
- Rosephil Muros
- Claire Helery Noel

---

## Final Notes

This version represents the completed Milestone 2 OOP refactoring of the MotorPH Payroll System.

All computation logic is centralized.  
All persistence is abstracted.  
All UI components are logic-free.  
The architecture strictly follows the approved MS1 design.  

The system is fully tested and ready for evaluation.
