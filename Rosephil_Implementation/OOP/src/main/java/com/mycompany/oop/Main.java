/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.oop;

import javax.swing.SwingUtilities;
import com.mycompany.oop.view.LoginFrame;

public class Main {

    public static void main(String[] args) {

        // Launch GUI safely inside the Swing Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}

    
    
        
// Encapsulation → Leave data protected
// Abstraction → LeaveService handles logic
// Inheritance → RegularEmployee extends Employee
// Polymorphism → compute methods called via Employee reference
        
//Class is a template, this is where we put/create objects.
//Object is a real instance created from a class.
//Employee emp = new Employee(...);
//Create a new Employee object, and store a reference to it in a variable called emp.
//Constructor is a special method that runs when an object is created (e.g public Employee(int employeeId, String firstName, ...)
//Method is a function inside a class that performs an action. I used methods getter to read data, and setter that changes data. While contructor creates object.
//int is a data type that stores whole number (e.g int employeeId, int hoursWorked)


/*
CURRENT VERSION
• Repository Pattern
• Service Layer Pattern
• Template Method Pattern
• Polymorphism-ready Employee hierarchy
• Cutoff-based payroll cycle
• Duplicate prevention
• Demo reset support
• HR view + Employee self-service
*/
