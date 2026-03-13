package com.mycompany.oop.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

public class Attendance {

    private int employeeId;
    private String lastName;
    private String firstName;
    private String date;       // MM/dd/yyyy
    private String logIn;      // H:mm
    private String logOut;     // H:mm (empty if not clocked out)

    public Attendance(int employeeId, String lastName, String firstName,
                      String date, String logIn, String logOut) {

        this.employeeId = employeeId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.date = date;
        this.logIn = logIn;
        this.logOut = logOut;
    }

    // ================= GETTERS =================

    public int getEmployeeId() { return employeeId; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getDate() { return date; }
    public String getLogIn() { return logIn; }
    public String getLogOut() { return logOut; }

    // ================= SETTERS =================

    public void setLogOut(String logOut) { this.logOut = logOut; }

    // ================= HOURS CALCULATION =================

    public double calculateHoursWorked() {

        if (logIn == null || logIn.isEmpty()
                || logOut == null || logOut.isEmpty()) {
            return 0;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
            LocalTime in = LocalTime.parse(logIn, formatter);
            LocalTime out = LocalTime.parse(logOut, formatter);

            long minutes = Duration.between(in, out).toMinutes();
            if (minutes < 0) minutes = 0;

            return minutes / 60.0;

        } catch (Exception e) {
            return 0;
        }
    }
}
