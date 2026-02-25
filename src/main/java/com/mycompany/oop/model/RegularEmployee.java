/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.model;

public class RegularEmployee extends Employee {
    //RegularEmployee = a real employee type that can be paid    

    
    
    public RegularEmployee(
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

    ) {
        
        super(
                employeeId, 
                firstName, 
                lastName, 
                position, 
                employmentStatus, 
                basicSalary, 
                allowance, 
                hourlyRate,
                username,
                password,
                role
        
        );

                

    }    
    
    
   //ABSTRACT METHOD
    
    @Override
    public double computeGrossSalary() {
        return getBasicSalary() + getAllowance();
    } 
    
    @Override
    public double computeDeductions() {
        return computeGrossSalary() * 0.10; // sample deduction only
    }

    @Override
    public double computeNetSalary() {
        return computeGrossSalary() - computeDeductions();
    }
    

}
   




   




//class RegularEmployee 
//you are defining a specific type of employee

//extends Employee
//RegularEmployee inherits all fields & methods of Employee
//avoids code duplication

//implements Payables
//Java ENFORCES that this class must implement payroll methods
//this is abstraction

//interface enables polymorphism
//abstract enables inheritance


//Motorph
//1. Create your interfaces (contracts). List down all your methods for calculation, and operations (CRUD).
//2. Create your "template" (parent class). Change this to an abstract class. Employee java. 
//3. Put in your attributes (Employee Data), create properties (getters and setters), create constructors, and override methods from the implemented interfaces. 
//4. Create classes for all your user types (HR, Finance, IT, Admin, etc.).
//5. Inherit your template (abstract parent class) by using extends too all your user types. 
//6. If you have extra interfaces (HR Operations, Admin Operations), implement them to the specific classes that need them.


