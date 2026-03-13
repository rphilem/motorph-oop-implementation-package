/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.service;

import com.mycompany.oop.model.AttendanceRecord;
import com.mycompany.oop.repository.AttendanceRepository;
import com.mycompany.oop.repository.CsvAttendanceRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AttendanceService {

    private AttendanceRepository repository;

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
}
