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
 







//OOP PRINCIPLES//

// 1. ENCAPSULATION

//How does encapsulation happen here?
//Encapsulation is applied by making all fields private and controlling access through public getters and setters with validation, ensuring that employee data cannot be modified directly or incorrectly.

//Why use setters instead of public fields?
//Setters allow us to enforce business rules and protect data integrity by rejecting invalid values.

//Why is Employee only storing data?”
//The Employee class follows single responsibility and acts as a model, while payroll logic will be handled by service classes.

//Reflection:
//Encapsulation is the practice of organizing code by protecting an object’s data and controlling how it is accessed. 
//It involves deciding which attributes should be hidden and allowing interaction with them only through public methods such as getters and setters, 
//ensuring data integrity and preventing direct modification.

//Encapsulation is hiding an object’s data and allowing access to it only through controlled methods like getters and setters.

//Encapsulation is an OOP principle that hides an object’s data by making fields private and controls access to them through public methods such as getters and setters.

//When to make attribute public?
//When to make attribute private?



// 2. ABSTRACTION

//Involves presenting only the essential attributes and hiding unnecessary information.
//Primary objective is to hide implementation details from users.
//In Java, it can be implemented using 2 key words - abstract and interface.

//Encapsulation = protecting the data
//Abstraction = protecting the code logic 

//Abstract is a non-access modifier (different from public or private)
//When a class or method is declared abstract, it means that they are partially implemented and the incomplete parts can be filled in  later.
//Abstract classes can have member variables with access control but they can't be instantitated directly.

//Interface provide total abstraction (pure abstract) by defining a set of methods in the class but without implementation details.
//Cannot have member variables anbd cannot be instantiated directly.
//Interface is not a class.
//Interface is always public.

// 3. Polymorphism
// Static (compile-time polymorphism) or method Overloading
// Dynamic (runtime polymorphism or method Overriding

/*
EMPLOYEE – ABSTRACT TEMPLATE UPDATE SUMMARY

Purpose:
Acts as payroll template using Template Method Pattern.

Key Structure:
Abstract computeGrossSalary()
Abstract computeDeductions()
Concrete computeNetSalary()

Enhancements:
Centralized net salary logic
Validation in setters
Supports polymorphic payroll computation

Why Important:
Allows future employee types:
Contractual
Part-time
Probationary
To override salary logic differently.

Design Pattern:
Template Method + Polymorphism

Scalability:
PayrollProcessor can adapt per subclass behavior automatically.
*/