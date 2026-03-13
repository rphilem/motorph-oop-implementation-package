package com.mycompany.oop.repository;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.RegularEmployee;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvEmployeeRepository implements EmployeeRepository {

    private final String filePath = "src/main/resources/employees.csv";

    private static final String CSV_HEADER =
            "employeeId,lastName,firstName,birthday,address,phoneNumber,email," +
            "sssNumber,philhealthNumber,tinNumber,pagibigNumber," +
            "status,position,immediateSupervisor," +
            "basicSalary,riceSubsidy,phoneAllowance,clothingAllowance,hourlyRate," +
            "username,password,role";

    // ================= ADD =================
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
    @Override
    public void updateEmployee(Employee updatedEmployee) {

        List<Employee> employees = getAllEmployees();

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(filePath))) {

            bw.write(CSV_HEADER);
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
    @Override
    public void deleteEmployee(int employeeId) {

        List<Employee> employees = getAllEmployees();

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(filePath))) {

            bw.write(CSV_HEADER);
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

        return getAllEmployees().stream()
                .filter(emp -> emp.getEmployeeId() == employeeId)
                .findFirst()
                .orElse(null);
    }

    // ================= READ ALL =================
    @Override
    public List<Employee> getAllEmployees() {

        List<Employee> employees = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new FileReader(filePath))) {

            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {

                String[] d = line.split(",");
                if (d.length < 22) continue;

                Employee employee = new RegularEmployee(
                        Integer.parseInt(d[0].trim()),    // employeeId
                        d[2].trim(),                       // firstName
                        d[1].trim(),                       // lastName
                        d[3].trim(),                       // birthday
                        d[4].trim(),                       // address
                        d[5].trim(),                       // phoneNumber
                        d[6].trim(),                       // email
                        d[7].trim(),                       // sssNumber
                        d[8].trim(),                       // philhealthNumber
                        d[9].trim(),                       // tinNumber
                        d[10].trim(),                      // pagibigNumber
                        d[11].trim(),                      // status
                        d[12].trim(),                      // position
                        d[13].trim(),                      // immediateSupervisor
                        Double.parseDouble(d[14].trim()),  // basicSalary
                        Double.parseDouble(d[15].trim()),  // riceSubsidy
                        Double.parseDouble(d[16].trim()),  // phoneAllowance
                        Double.parseDouble(d[17].trim()),  // clothingAllowance
                        Double.parseDouble(d[18].trim()),  // hourlyRate
                        d[19].trim(),                      // username
                        d[20].trim(),                      // password
                        d[21].trim()                       // role
                );

                employees.add(employee);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return employees;
    }

    // ================= HELPER =================
    private String formatEmployee(Employee emp) {

        return emp.getEmployeeId() + "," +
                emp.getLastName() + "," +
                emp.getFirstName() + "," +
                emp.getBirthday() + "," +
                emp.getAddress() + "," +
                emp.getPhoneNumber() + "," +
                emp.getEmail() + "," +
                emp.getSssNumber() + "," +
                emp.getPhilhealthNumber() + "," +
                emp.getTinNumber() + "," +
                emp.getPagibigNumber() + "," +
                emp.getEmploymentStatus() + "," +
                emp.getPosition() + "," +
                emp.getImmediateSupervisor() + "," +
                emp.getBasicSalary() + "," +
                emp.getRiceSubsidy() + "," +
                emp.getPhoneAllowance() + "," +
                emp.getClothingAllowance() + "," +
                emp.getHourlyRate() + "," +
                emp.getUsername() + "," +
                emp.getPassword() + "," +
                emp.getRole();
    }
}
