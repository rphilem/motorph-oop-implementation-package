/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.service;

import com.mycompany.oop.model.AttendanceRecord;
import com.mycompany.oop.repository.AttendanceRepository;
import com.mycompany.oop.repository.CsvAttendanceRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

public class AttendanceService {

    private AttendanceRepository repository;

    private static final DateTimeFormatter ISO_DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter MONTH_FMT =
            DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH);

    public AttendanceService() {
        this.repository = new CsvAttendanceRepository();
    }

    public void timeIn(int employeeId) {
        String today = LocalDate.now().toString();
        String now = LocalTime.now().withNano(0).toString();

        AttendanceRecord existing = repository.findByEmployeeAndDate(employeeId, today);

        if (existing == null) {
            repository.saveAttendance(new AttendanceRecord(employeeId, today, now, ""));
        }
    }

    public void timeOut(int employeeId) {
        String today = LocalDate.now().toString();
        String now = LocalTime.now().withNano(0).toString();

        AttendanceRecord existing = repository.findByEmployeeAndDate(employeeId, today);

        if (existing != null && (existing.getTimeOut() == null || existing.getTimeOut().isEmpty())) {
            existing.setTimeOut(now);
            repository.updateAttendance(existing);
        }
    }

    public List<AttendanceRecord> getAttendanceHistory(int employeeId) {
        return repository.findByEmployeeId(employeeId);
    }

    // ================= PAYROLL SUPPORT =================

    public double getHoursForCutoff(int employeeId, String cutoffPeriod) {
        LocalDate[] range = parseCutoffRange(cutoffPeriod);
        LocalDate start = range[0];
        LocalDate end = range[1];

        double totalHours = 0.0;

        for (AttendanceRecord record : repository.findByEmployeeId(employeeId)) {
            try {
                LocalDate recordDate = LocalDate.parse(record.getDate(), ISO_DATE_FMT);

                if (!recordDate.isBefore(start) && !recordDate.isAfter(end)) {
                    totalHours += calculateHoursWorked(record);
                }
            } catch (Exception e) {
                // skip invalid attendance rows
            }
        }

        return totalHours;
    }

    public List<String> getAvailableCutoffs() {
        Set<YearMonth> months = new TreeSet<>();

        // collect months from all employees' records
        // if your repository later gets a findAll() method, use that instead
        for (int employeeId = 10001; employeeId <= 99999; employeeId++) {
            List<AttendanceRecord> records = repository.findByEmployeeId(employeeId);

            for (AttendanceRecord record : records) {
                try {
                    LocalDate date = LocalDate.parse(record.getDate(), ISO_DATE_FMT);
                    months.add(YearMonth.from(date));
                } catch (Exception e) {
                    // skip invalid rows
                }
            }
        }

        List<String> cutoffs = new ArrayList<>();

        for (YearMonth ym : months) {
            String prefix = ym.format(MONTH_FMT) + "-" + ym.getYear();
            cutoffs.add(prefix + "-1st");
            cutoffs.add(prefix + "-2nd");
        }

        return cutoffs;
    }

    private LocalDate[] parseCutoffRange(String cutoffPeriod) {
        String[] parts = cutoffPeriod.split("-");
        Month month = Month.from(MONTH_FMT.parse(parts[0]));
        int year = Integer.parseInt(parts[1]);
        boolean firstHalf = parts[2].equals("1st");

        LocalDate start;
        LocalDate end;

        if (firstHalf) {
            start = LocalDate.of(year, month, 1);
            end = LocalDate.of(year, month, 15);
        } else {
            start = LocalDate.of(year, month, 16);
            end = YearMonth.of(year, month).atEndOfMonth();
        }

        return new LocalDate[]{start, end};
    }

    private double calculateHoursWorked(AttendanceRecord record) {
        try {
            if (record.getTimeIn() == null || record.getTimeIn().isEmpty()
                    || record.getTimeOut() == null || record.getTimeOut().isEmpty()) {
                return 0.0;
            }

            LocalTime timeIn = LocalTime.parse(record.getTimeIn());
            LocalTime timeOut = LocalTime.parse(record.getTimeOut());

            long minutes = Duration.between(timeIn, timeOut).toMinutes();

            if (minutes < 0) {
                return 0.0;
            }

            return minutes / 60.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
}