/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.repository;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.RegularEmployee;
import java.util.List;
// List allows us to store multiple Employee objects
import java.util.ArrayList;
import java.io.*;


public class CsvEmployeeRepository implements EmployeeRepository{
// @Override means:
// This method is required by the interface.
// If we remove it, the compiler will complain.
    
    
   @Override
    public void addEmployee(Employee employee) {
    
    }               
    
    @Override
    public void updateEmployee(Employee employee) {
    
    }                      

    @Override
    public void deleteEmployee(int employeeId) {
    
    }                            
    
   @Override
    public Employee findEmployee(int employeeId) {
    
        return null;
    }
    
    @Override
    public List<Employee> getAllEmployees() {

        List<Employee> employees = new ArrayList<>();

        try (
            InputStream input = getClass()
                    .getClassLoader()
                    .getResourceAsStream("employees.csv");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(input)
            )
        ) {        
                String line;
                
                // Skip the header row (first time)
                br.readLine();
                
                while ((line = br.readLine()) !=null) {
                System.out.println("Reading line: " + line);
                    
                    String[] data = line.split(",");
                    
                    // Extract and convert values
                    int employeeId = Integer.parseInt(data[0].trim());
                    String firstName = data[1].trim();
                    String lastName = data[2].trim();
                    String position = data[3].trim();
                    String employmentStatus = data[4].trim();
                    double basicSalary = Double.parseDouble(data[5].trim());
                    double allowance = Double.parseDouble(data[6].trim());
                    double hourlyRate = Double.parseDouble(data[7].trim());
                    String username = data[8].trim();
                    String password = data[9].trim();
                    String role = data[10].trim();
                    
                    // Create RegularEmployee object
                    Employee employee = new RegularEmployee(
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
                    
                    // Add to list
                    employees.add(employee);
                }
                
                                              
        } catch (IOException e) {
        
            e.printStackTrace();
        }
        
        return employees;
    }     
    
}
