/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.model;

public abstract class Employee implements Payables {

    private int employeeId;          
    private String firstName;        
    private String lastName;         
    private String position;         
    private String employmentStatus; 
    private double basicSalary;      
    private double allowance;        
    private double hourlyRate;       
    private String username;         
    private String password;         
    private String role;             

    // Constructor
    public Employee(
        int employeeId,
        String firstName,
        String lastName,
        String position,
        String employmentStatus,
        double basicSalary,
        double allowance,
        double hourlyRate,
        String username,
        String password,
        String role
    ){

        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;

        this.position = position;
        this.employmentStatus = employmentStatus;

        this.basicSalary = basicSalary >= 0 ? basicSalary : 0;
        this.allowance = allowance >= 0 ? allowance : 0;
        this.hourlyRate = hourlyRate >= 0 ? hourlyRate : 0;

        this.username = username;
        this.password = password;
        this.role = role;
    }

    // ================= GETTERS =================

    public int getEmployeeId() {
        return employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPosition() {
        return position;
    }    

    public String getEmploymentStatus() {
        return employmentStatus;
    } 

    public double getBasicSalary() {
        return basicSalary;
    } 

    public double getAllowance() {
        return allowance;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }
    
    public String getUsername() {
        return username;
    }

    // Keep for now (but note: in real systems passwords must be hashed)
    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    // ================= SETTERS =================

    public void setPosition(String position) {
        if (position != null && !position.isEmpty()) {
            this.position = position;
        }
    }

    public void setEmploymentStatus(String employmentStatus) {
        if (employmentStatus != null && !employmentStatus.isEmpty()) {
            this.employmentStatus = employmentStatus;
        }
    }

    public void setBasicSalary(Double basicSalary) {
        if (basicSalary != null && basicSalary >= 0) {
            this.basicSalary = basicSalary;
        }
    }

    public void setAllowance(Double allowance) {
        if (allowance != null && allowance >= 0) {
            this.allowance = allowance;
        }
    }
    
    public void setHourlyRate(Double hourlyRate) {
        if (hourlyRate != null && hourlyRate >= 0) {
            this.hourlyRate = hourlyRate;
        }
    }

    public void setUsername(String username) {
        if (username != null && !username.isEmpty()) {
            this.username = username;
        }
    }

    public void setPassword(String password) {
        if (password != null && !password.isEmpty()) {
            this.password = password;
        }
    }       
    
    public void setRole(String role) {
        if (role != null && !role.isEmpty()) {
            this.role = role;
        }
    }

    // ================= PAYROLL TEMPLATE METHODS =================

    @Override 
    public abstract double computeGrossSalary();
    
    @Override
    public abstract double computeDeductions();
    
    // Template Method Pattern
    // Net salary formula is centralized here to avoid duplication
    @Override
    public double computeNetSalary() {
        return computeGrossSalary() - computeDeductions();
    }
}
 





/*
EMPLOYEE – CORE DOMAIN MODEL

Purpose:
Represents an employee entity in the MotorPH Payroll System.
This class serves as the foundational data model that stores all
employee-related information used across the system.

Core Responsibilities:
• Store employee identity and employment information
• Maintain salary-related attributes (basic salary, allowance, hourly rate)
• Manage login credentials and system role
• Provide payroll template methods used by payroll computation services

Key Attributes:
• Employee ID
• Personal information (first name, last name)
• Employment details (position, employment status)
• Compensation data (basic salary, allowance, hourly rate)
• System credentials (username, password)
• Access control role (Admin, HR, Finance, IT, Employee)

Design Role in the Architecture:
The Employee class is part of the Model layer in the system's layered
architecture and acts as the primary domain object used by:

• Repository layer – for CSV data persistence
• Service layer – for business logic operations
• UI layer – for displaying employee information

OOP Concepts Implemented:
• Encapsulation
  Employee attributes are private and accessed through controlled
  getters and setters.

• Abstraction
  The class defines abstract payroll methods that must be implemented
  by concrete employee types.

• Inheritance
  Specific employee types such as RegularEmployee extend this class.

• Polymorphism
  Different employee types can implement their own payroll behaviors.

Payroll Design Principle:
This class defines the payroll computation template but does not
implement government deduction logic directly. Actual payroll
calculations are delegated to the PayrollProcessor service class.

Template Methods:
• computeGrossSalary()
• computeDeductions()
• computeNetSalary()

The computeNetSalary() method follows the Template Method Pattern,
ensuring consistent net salary calculation across employee types.

System Role:
Acts as the central data structure linking employee information,
authentication, payroll processing, and role-based access control.
*/