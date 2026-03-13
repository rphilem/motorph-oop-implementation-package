package com.mycompany.oop.model;

public abstract class Employee implements Payable {

    private int employeeId;
    private String firstName;
    private String lastName;
    private String birthday;
    private String address;
    private String phoneNumber;
    private String email;
    private String sssNumber;
    private String philhealthNumber;
    private String tinNumber;
    private String pagibigNumber;
    private String employmentStatus;
    private String position;
    private String immediateSupervisor;
    private double basicSalary;
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;
    private double hourlyRate;
    private String username;
    private String password;
    private String role;

    public Employee(
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
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.sssNumber = sssNumber;
        this.philhealthNumber = philhealthNumber;
        this.tinNumber = tinNumber;
        this.pagibigNumber = pagibigNumber;
        this.employmentStatus = employmentStatus;
        this.position = position;
        this.immediateSupervisor = immediateSupervisor;
        this.basicSalary = basicSalary >= 0 ? basicSalary : 0;
        this.riceSubsidy = riceSubsidy >= 0 ? riceSubsidy : 0;
        this.phoneAllowance = phoneAllowance >= 0 ? phoneAllowance : 0;
        this.clothingAllowance = clothingAllowance >= 0 ? clothingAllowance : 0;
        this.hourlyRate = hourlyRate >= 0 ? hourlyRate : 0;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // ================= GETTERS =================

    public int getEmployeeId() { return employeeId; }
    public String getEmployeeNumber() { return String.valueOf(employeeId); }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getBirthday() { return birthday; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getSssNumber() { return sssNumber; }
    public String getPhilhealthNumber() { return philhealthNumber; }
    public String getTinNumber() { return tinNumber; }
    public String getPagibigNumber() { return pagibigNumber; }
    public String getEmploymentStatus() { return employmentStatus; }
    public String getPosition() { return position; }
    public String getImmediateSupervisor() { return immediateSupervisor; }
    public double getBasicSalary() { return basicSalary; }
    public double getRiceSubsidy() { return riceSubsidy; }
    public double getPhoneAllowance() { return phoneAllowance; }
    public double getClothingAllowance() { return clothingAllowance; }
    public double getTotalAllowance() { return riceSubsidy + phoneAllowance + clothingAllowance; }
    public double getHourlyRate() { return hourlyRate; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    // ================= SETTERS =================

    public void setBasicSalary(String salary) {
        if (salary != null && !salary.isEmpty()) {
            double val = Double.parseDouble(salary);
            if (val >= 0) this.basicSalary = val;
        }
    }

    public void setHourlyRate(double rate) {
        if (rate > 0) this.hourlyRate = rate;
    }

    public void setBirthday(String birthday) {
        if (birthday != null && !birthday.isEmpty()) this.birthday = birthday;
    }

    public void setAddress(String address) {
        if (address != null && !address.isEmpty()) this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.isEmpty()) this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        if (email != null && !email.isEmpty()) this.email = email;
    }

    public void setSssNumber(String sssNumber) {
        if (sssNumber != null && !sssNumber.isEmpty()) this.sssNumber = sssNumber;
    }

    public void setPhilhealthNumber(String philhealthNumber) {
        if (philhealthNumber != null && !philhealthNumber.isEmpty()) this.philhealthNumber = philhealthNumber;
    }

    public void setTinNumber(String tinNumber) {
        if (tinNumber != null && !tinNumber.isEmpty()) this.tinNumber = tinNumber;
    }

    public void setPagibigNumber(String pagibigNumber) {
        if (pagibigNumber != null && !pagibigNumber.isEmpty()) this.pagibigNumber = pagibigNumber;
    }

    public void setPosition(String position) {
        if (position != null && !position.isEmpty()) this.position = position;
    }

    public void setEmploymentStatus(String employmentStatus) {
        if (employmentStatus != null && !employmentStatus.isEmpty()) this.employmentStatus = employmentStatus;
    }

    public void setImmediateSupervisor(String immediateSupervisor) {
        if (immediateSupervisor != null) this.immediateSupervisor = immediateSupervisor;
    }

    public void setRiceSubsidy(Double riceSubsidy) {
        if (riceSubsidy != null && riceSubsidy >= 0) this.riceSubsidy = riceSubsidy;
    }

    public void setPhoneAllowance(Double phoneAllowance) {
        if (phoneAllowance != null && phoneAllowance >= 0) this.phoneAllowance = phoneAllowance;
    }

    public void setClothingAllowance(Double clothingAllowance) {
        if (clothingAllowance != null && clothingAllowance >= 0) this.clothingAllowance = clothingAllowance;
    }

    public void setUsername(String username) {
        if (username != null && !username.isEmpty()) this.username = username;
    }

    public void setPassword(String password) {
        if (password != null && !password.isEmpty()) this.password = password;
    }

    public void setRole(String role) {
        if (role != null && !role.isEmpty()) this.role = role;
    }

    // ================= PAYABLE CONTRACT =================

    @Override
    public abstract double getGrossSalary();

    @Override
    public abstract double getDeductions();

    @Override
    public double getNetSalary() {
        return getGrossSalary() - getDeductions();
    }
}
