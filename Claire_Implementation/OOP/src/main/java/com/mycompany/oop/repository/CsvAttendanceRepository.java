package com.mycompany.oop.repository;

import com.mycompany.oop.model.Attendance;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CsvAttendanceRepository implements AttendanceRepository {

    private final String filePath = "src/main/resources/motorph_attendance_records.csv";

    private static final String CSV_HEADER =
            "Employee #,Last Name,First Name,Date,Log In,Log Out";

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("MM/dd/yyyy");

    // ================= READ ALL =================
    @Override
    public List<Attendance> getAllRecords() {

        List<Attendance> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {

                String[] d = line.split(",");
                if (d.length < 5) continue;

                Attendance record = new Attendance(
                        Integer.parseInt(d[0].trim()),
                        d[1].trim(),
                        d[2].trim(),
                        d[3].trim(),
                        d[4].trim(),
                        d.length > 5 ? d[5].trim() : ""
                );

                records.add(record);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return records;
    }

    // ================= FIND BY EMPLOYEE =================
    @Override
    public List<Attendance> findByEmployeeId(int employeeId) {

        return getAllRecords().stream()
                .filter(r -> r.getEmployeeId() == employeeId)
                .collect(Collectors.toList());
    }

    // ================= FIND TODAY =================
    @Override
    public Attendance findTodayRecord(int employeeId) {

        String today = LocalDate.now().format(DATE_FMT);

        return getAllRecords().stream()
                .filter(r -> r.getEmployeeId() == employeeId
                        && r.getDate().equals(today))
                .findFirst()
                .orElse(null);
    }

    // ================= ADD =================
    @Override
    public void addRecord(Attendance record) {

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(filePath, true))) {

            bw.write(formatRecord(record));
            bw.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= UPDATE (clock out) =================
    @Override
    public void updateRecord(Attendance updated) {

        List<Attendance> records = getAllRecords();

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(filePath))) {

            bw.write(CSV_HEADER);
            bw.newLine();

            for (Attendance r : records) {

                if (r.getEmployeeId() == updated.getEmployeeId()
                        && r.getDate().equals(updated.getDate())) {
                    r = updated;
                }

                bw.write(formatRecord(r));
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= HELPER =================
    private String formatRecord(Attendance r) {

        return r.getEmployeeId() + "," +
                r.getLastName() + "," +
                r.getFirstName() + "," +
                r.getDate() + "," +
                r.getLogIn() + "," +
                r.getLogOut();
    }
}
