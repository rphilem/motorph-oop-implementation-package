package com.mycompany.oop.service;

import com.mycompany.oop.model.Attendance;
import com.mycompany.oop.model.Employee;
import com.mycompany.oop.repository.AttendanceRepository;
import com.mycompany.oop.repository.CsvAttendanceRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AttendanceService {

    private AttendanceRepository repository;

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("H:mm");
    private static final DateTimeFormatter MONTH_FMT =
            DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH);

    public AttendanceService() {
        this.repository = new CsvAttendanceRepository();
    }

    // ================= CLOCK IN =================
    public Attendance clockIn(Employee employee) {

        Attendance existing = repository.findTodayRecord(employee.getEmployeeId());

        if (existing != null) {
            throw new IllegalStateException("Already clocked in today at " + existing.getLogIn());
        }

        String today = LocalDate.now().format(DATE_FMT);
        String now = LocalTime.now().format(TIME_FMT);

        Attendance record = new Attendance(
                employee.getEmployeeId(),
                employee.getLastName(),
                employee.getFirstName(),
                today,
                now,
                ""
        );

        repository.addRecord(record);
        return record;
    }

    // ================= CLOCK OUT =================
    public Attendance clockOut(Employee employee) {

        Attendance existing = repository.findTodayRecord(employee.getEmployeeId());

        if (existing == null) {
            throw new IllegalStateException("Not clocked in today.");
        }

        if (existing.getLogOut() != null && !existing.getLogOut().isEmpty()) {
            throw new IllegalStateException("Already clocked out today at " + existing.getLogOut());
        }

        String now = LocalTime.now().format(TIME_FMT);
        existing.setLogOut(now);
        repository.updateRecord(existing);

        return existing;
    }

    // ================= STATUS =================
    public String getTodayStatus(int employeeId) {

        Attendance today = repository.findTodayRecord(employeeId);

        if (today == null) {
            return "NOT_CLOCKED_IN";
        }

        if (today.getLogOut() == null || today.getLogOut().isEmpty()) {
            return "CLOCKED_IN";
        }

        return "DONE";
    }

    public Attendance getTodayRecord(int employeeId) {
        return repository.findTodayRecord(employeeId);
    }

    // ================= HISTORY =================
    public List<Attendance> getAttendanceHistory(int employeeId) {
        return repository.findByEmployeeId(employeeId);
    }

    // ================= TOTAL HOURS =================
    public double calculateTotalHours(int employeeId) {

        return repository.findByEmployeeId(employeeId).stream()
                .mapToDouble(Attendance::calculateHoursWorked)
                .sum();
    }

    // ================= CUTOFF-BASED HOURS =================

    public double getHoursForCutoff(int employeeId, String cutoffPeriod) {

        LocalDate[] range = parseCutoffRange(cutoffPeriod);
        LocalDate start = range[0];
        LocalDate end = range[1];

        return repository.findByEmployeeId(employeeId).stream()
                .filter(a -> {
                    try {
                        LocalDate date = LocalDate.parse(a.getDate(), DATE_FMT);
                        return !date.isBefore(start) && !date.isAfter(end);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .mapToDouble(Attendance::calculateHoursWorked)
                .sum();
    }

    public List<String> getAvailableCutoffs() {

        List<Attendance> all = repository.getAllRecords();
        Set<YearMonth> months = new TreeSet<>();

        for (Attendance a : all) {
            try {
                LocalDate date = LocalDate.parse(a.getDate(), DATE_FMT);
                months.add(YearMonth.from(date));
            } catch (Exception e) {
                // skip invalid dates
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

    // ================= CUTOFF PARSER =================

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
}
