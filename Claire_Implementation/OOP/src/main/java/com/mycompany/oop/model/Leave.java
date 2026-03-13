package com.mycompany.oop.model;

public class Leave {

    private final int leaveId;
    private final int employeeId;
    private final String leaveType;
    private final String startDate;
    private final String endDate;
    private final String reason;
    private String status;
    private final String filedDate;
    private String approvedBy;
    private String remarks;

    public Leave(int leaveId,
                 int employeeId,
                 String leaveType,
                 String startDate,
                 String endDate,
                 String reason,
                 String status,
                 String filedDate,
                 String approvedBy,
                 String remarks) {

        this.leaveId = leaveId;
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = status;
        this.filedDate = filedDate;
        this.approvedBy = approvedBy;
        this.remarks = remarks;
    }

    // ================= GETTERS =================

    public int getLeaveId() { return leaveId; }
    public int getEmployeeId() { return employeeId; }
    public String getLeaveType() { return leaveType; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getReason() { return reason; }
    public String getStatus() { return status; }
    public String getFiledDate() { return filedDate; }
    public String getApprovedBy() { return approvedBy; }
    public String getRemarks() { return remarks; }

    public int getLeaveBalance() {
        try {
            java.time.LocalDate start = java.time.LocalDate.parse(startDate);
            java.time.LocalDate end = java.time.LocalDate.parse(endDate);
            return (int) java.time.temporal.ChronoUnit.DAYS.between(start, end) + 1;
        } catch (Exception e) {
            return 0;
        }
    }

    // ================= SETTERS (mutable fields only) =================

    public void setStatus(String status) {
        if (status != null && !status.isEmpty()) this.status = status;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
