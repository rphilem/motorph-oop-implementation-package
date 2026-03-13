package com.mycompany.oop.repository;

import java.io.*;
import java.util.*;
import com.mycompany.oop.model.Leave;

public class CsvLeaveRepository implements LeaveRepository {

    private final String filePath = "src/main/resources/leaves.csv";

    @Override
    public void addLeave(Leave leave) {

        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(formatLeave(leave));
            bw.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateLeave(Leave updatedLeave) {

        List<Leave> leaves = getAllLeaves();

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(filePath))) {

            for (Leave leave : leaves) {

                if (leave.getLeaveId() ==
                    updatedLeave.getLeaveId()) {

                    leave = updatedLeave;
                }

                bw.write(formatLeave(leave));
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Leave findLeave(int leaveId) {

        return getAllLeaves().stream()
                .filter(leave -> leave.getLeaveId() == leaveId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Leave> getAllLeaves() {

        List<Leave> leaves = new ArrayList<>();

        try (BufferedReader br =
                     new BufferedReader(
                             new FileReader(filePath))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",", -1);
                if (data.length < 7) continue;

                // Handle old format (7 fields) and new format (10 fields)
                String filedDate = data.length > 7 ? data[7] : "";
                String approvedBy = data.length > 8 ? data[8] : "";
                String remarks = data.length > 9 ? data[9] : "";

                leaves.add(new Leave(
                        Integer.parseInt(data[0].trim()),
                        Integer.parseInt(data[1].trim()),
                        data[2].trim(),
                        data[3].trim(),
                        data[4].trim(),
                        data[5].trim(),
                        data[6].trim(),
                        filedDate.trim(),
                        approvedBy.trim(),
                        remarks.trim()
                ));
            }

        } catch (IOException e) {
            // file may not exist yet
        }

        return leaves;
    }

    @Override
    public List<Leave> getLeavesByEmployee(int employeeId) {

        return getAllLeaves().stream()
                .filter(leave -> leave.getEmployeeId() == employeeId)
                .collect(java.util.stream.Collectors.toList());
    }

    private String formatLeave(Leave leave) {

        return leave.getLeaveId() + "," +
                leave.getEmployeeId() + "," +
                leave.getLeaveType() + "," +
                leave.getStartDate() + "," +
                leave.getEndDate() + "," +
                leave.getReason() + "," +
                leave.getStatus() + "," +
                leave.getFiledDate() + "," +
                leave.getApprovedBy() + "," +
                leave.getRemarks();
    }
}
