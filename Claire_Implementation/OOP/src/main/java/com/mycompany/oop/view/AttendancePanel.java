package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.mycompany.oop.model.Attendance;
import com.mycompany.oop.model.Employee;
import com.mycompany.oop.service.AttendanceService;

public class AttendancePanel extends JPanel {

    private AttendanceService service;
    private Employee employee;
    private JTable table;
    private JLabel statusLabel;
    private JButton clockInBtn;
    private JButton clockOutBtn;

    public AttendancePanel(Employee employee) {

        this.employee = employee;
        this.service = new AttendanceService();

        setLayout(new BorderLayout());
        setBackground(UITheme.BG);

        add(UITheme.createTitleBar("My Attendance"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.BG);
        content.setBorder(new EmptyBorder(16, 20, 16, 20));

        content.add(createClockPanel(), BorderLayout.NORTH);
        content.add(createTablePanel(), BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        updateStatus();
    }

    private JPanel createClockPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(18, 22, 18, 22)
        ));

        JLabel clockTitle = new JLabel("Today's Attendance");
        clockTitle.setFont(UITheme.FONT_SECTION);
        clockTitle.setForeground(UITheme.TEXT_PRIMARY);
        clockTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

        statusLabel = new JLabel("Checking status...");
        statusLabel.setFont(UITheme.FONT_BODY_BOLD);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        buttonRow.setBackground(Color.WHITE);

        clockInBtn = UITheme.createAccentButton("Clock In");
        clockOutBtn = UITheme.createButton("Clock Out");

        clockInBtn.setPreferredSize(new Dimension(120, 36));
        clockOutBtn.setPreferredSize(new Dimension(120, 36));

        clockInBtn.addActionListener(e -> doClockIn());
        clockOutBtn.addActionListener(e -> doClockOut());

        buttonRow.add(clockInBtn);
        buttonRow.add(clockOutBtn);

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setBackground(Color.WHITE);
        topSection.add(clockTitle, BorderLayout.NORTH);
        topSection.add(statusLabel, BorderLayout.CENTER);

        panel.add(topSection, BorderLayout.NORTH);
        panel.add(buttonRow, BorderLayout.SOUTH);

        JPanel outerWrapper = new JPanel(new BorderLayout());
        outerWrapper.setBackground(UITheme.BG);
        outerWrapper.setBorder(new EmptyBorder(0, 0, 16, 0));
        outerWrapper.add(panel, BorderLayout.CENTER);

        return outerWrapper;
    }

    private JScrollPane createTablePanel() {

        table = new JTable();
        UITheme.styleTable(table);

        refreshTable();

        return UITheme.createTableScrollPane(table);
    }

    private void refreshTable() {

        List<Attendance> history = service.getAttendanceHistory(
                employee.getEmployeeId());

        String[] cols = {"Date", "Log In", "Log Out", "Hours Worked"};

        Object[][] data = new Object[history.size()][4];

        for (int i = 0; i < history.size(); i++) {
            Attendance a = history.get(i);
            data[i][0] = a.getDate();
            data[i][1] = a.getLogIn();
            data[i][2] = (a.getLogOut() != null && !a.getLogOut().isEmpty())
                    ? a.getLogOut() : "--";
            data[i][3] = String.format("%.2f", a.calculateHoursWorked());
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model);
    }

    private void doClockIn() {
        try {
            Attendance record = service.clockIn(employee);
            JOptionPane.showMessageDialog(this,
                    "Clocked in at " + record.getLogIn());
            updateStatus();
            refreshTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Clock In",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void doClockOut() {
        try {
            Attendance record = service.clockOut(employee);
            JOptionPane.showMessageDialog(this,
                    "Clocked out at " + record.getLogOut() +
                            "\nHours worked: " + String.format("%.2f",
                            record.calculateHoursWorked()));
            updateStatus();
            refreshTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Clock Out",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateStatus() {

        String status = service.getTodayStatus(employee.getEmployeeId());

        switch (status) {
            case "NOT_CLOCKED_IN":
                statusLabel.setText("Not clocked in today");
                statusLabel.setForeground(UITheme.DANGER);
                clockInBtn.setEnabled(true);
                clockOutBtn.setEnabled(false);
                break;

            case "CLOCKED_IN":
                Attendance today = service.getTodayRecord(employee.getEmployeeId());
                statusLabel.setText("Clocked in at " + today.getLogIn());
                statusLabel.setForeground(UITheme.SUCCESS);
                clockInBtn.setEnabled(false);
                clockOutBtn.setEnabled(true);
                break;

            case "DONE":
                Attendance done = service.getTodayRecord(employee.getEmployeeId());
                statusLabel.setText("Done for today (" +
                        done.getLogIn() + " - " + done.getLogOut() + ")");
                statusLabel.setForeground(UITheme.TEXT_SECONDARY);
                clockInBtn.setEnabled(false);
                clockOutBtn.setEnabled(false);
                break;
        }
    }
}
