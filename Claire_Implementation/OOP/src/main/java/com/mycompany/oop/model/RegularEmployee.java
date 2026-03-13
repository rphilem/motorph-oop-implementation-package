package com.mycompany.oop.model;

public class RegularEmployee extends Employee {

    public RegularEmployee(
            int employeeId,
            String firstName,
            String lastName,
            String birthday,
            String address,
            String phoneNumber,
            String email,
            String sssNumber,
            String philhealthNumber,
            String tinNumber,
            String pagibigNumber,
            String employmentStatus,
            String position,
            String immediateSupervisor,
            double basicSalary,
            double riceSubsidy,
            double phoneAllowance,
            double clothingAllowance,
            double hourlyRate,
            String username,
            String password,
            String role
    ) {
        super(
                employeeId, firstName, lastName,
                birthday, address, phoneNumber, email,
                sssNumber, philhealthNumber, tinNumber, pagibigNumber,
                employmentStatus, position, immediateSupervisor,
                basicSalary, riceSubsidy, phoneAllowance, clothingAllowance,
                hourlyRate, username, password, role
        );
    }

    @Override
    public double getGrossSalary() {
        return getBasicSalary() + getTotalAllowance();
    }

    @Override
    public double getDeductions() {
        return getGrossSalary() * 0.10;
    }
}
