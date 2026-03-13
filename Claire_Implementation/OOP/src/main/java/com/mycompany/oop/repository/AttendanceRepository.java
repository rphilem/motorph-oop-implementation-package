package com.mycompany.oop.repository;

import com.mycompany.oop.model.Attendance;
import java.util.List;

public interface AttendanceRepository {

    List<Attendance> getAllRecords();

    List<Attendance> findByEmployeeId(int employeeId);

    Attendance findTodayRecord(int employeeId);

    void addRecord(Attendance record);

    void updateRecord(Attendance record);
}
