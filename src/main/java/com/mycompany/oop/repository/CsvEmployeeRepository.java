/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.repository;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.RegularEmployee;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvEmployeeRepository implements EmployeeRepository {

    // This is the real file path so we can READ and WRITE
    private final String filePath = "src/main/resources/employees.csv";


    // ================= ADD =================
    // Append to CSV
    @Override
    public void addEmployee(Employee employee) {

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(filePath, true))) {

            bw.write(formatEmployee(employee));
            bw.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // ================= UPDATE =================
    // Rewrite the CSV file with updated data
    @Override
    public void updateEmployee(Employee updatedEmployee) {

        List<Employee> employees = getAllEmployees();

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(filePath))) {

            // Write header again
            bw.write("employeeId,firstName,lastName,position,employmentStatus,basicSalary,allowance,hourlyRate,username,password,role");
            bw.newLine();

            for (Employee emp : employees) {

                if (emp.getEmployeeId() == updatedEmployee.getEmployeeId()) {
                    emp = updatedEmployee;
                }

                bw.write(formatEmployee(emp));
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // ================= DELETE =================
    // Rewrite CSV but skip the deleted employee
    @Override
    public void deleteEmployee(int employeeId) {

        List<Employee> employees = getAllEmployees();

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(filePath))) {

            // Write header again
            bw.write("employeeId,firstName,lastName,position,employmentStatus,basicSalary,allowance,hourlyRate,username,password,role");
            bw.newLine();

            for (Employee emp : employees) {

                if (emp.getEmployeeId() != employeeId) {
                    bw.write(formatEmployee(emp));
                    bw.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // ================= FIND =================
    @Override
    public Employee findEmployee(int employeeId) {

        List<Employee> employees = getAllEmployees();

        for (Employee emp : employees) {
            if (emp.getEmployeeId() == employeeId) {
                return emp;
            }
        }

        return null;
    }


    // ================= READ ALL =================
    @Override
    public List<Employee> getAllEmployees() {

        List<Employee> employees = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new FileReader(filePath))) {

            String line;

            // Skip header
            br.readLine();

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

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

                employees.add(employee);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return employees;
    }


    // ================= HELPER METHOD =================
    // This avoids repeating CSV formatting everywhere
    private String formatEmployee(Employee emp) {

        return emp.getEmployeeId() + "," +
                emp.getFirstName() + "," +
                emp.getLastName() + "," +
                emp.getPosition() + "," +
                emp.getEmploymentStatus() + "," +
                emp.getBasicSalary() + "," +
                emp.getAllowance() + "," +
                emp.getHourlyRate() + "," +
                emp.getUsername() + "," +
                emp.getPassword() + "," +
                emp.getRole();
    }
}






// This version:
// Reads from a real file path (so it can write back)
// Implements add
// Implements update
// Implements delete
// Implements find
// Keeps structure consistent

// Before:
// It was reading from resources stream (read-only)
// Add/update/delete were empty
// find always returned null

// Now:
// You are reading and writing to the real CSV file path
// Add appends to the file
// Update rewrites entire file with modified employee
// Delete rewrites file without the deleted employee
// Find searches properly
// formatEmployee() prevents duplicate string building everywhere
// Everything should now persist to CSV.