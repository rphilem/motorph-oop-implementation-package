/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import com.mycompany.oop.model.AttendanceRecord;
import com.mycompany.oop.model.Employee;
import com.mycompany.oop.service.AttendanceService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.util.List;

public class AttendancePanel extends JPanel {

    private Employee employee;
    private AttendanceService attendanceService;

    private JLabel statusLabel;
    private JTable table;
    private JButton timeInBtn;
    private JButton timeOutBtn;

    public AttendancePanel(Employee employee) {

        this.employee = employee;
        this.attendanceService = new AttendanceService();

        setLayout(new BorderLayout());
        setBackground(UITheme.BG);

        add(UITheme.createTitleBar("My Attendance"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.BG);
        content.setBorder(new EmptyBorder(16, 20, 16, 20));

        content.add(createTodayPanel(), BorderLayout.NORTH);
        content.add(createHistoryPanel(), BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        refreshAttendance();
    }

    private JPanel createTodayPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UITheme.BORDER),
                        new EmptyBorder(18, 22, 18, 22)
                )
        );

        JLabel title = new JLabel("Today's Attendance");
        title.setFont(UITheme.FONT_SECTION);
        title.setForeground(UITheme.TEXT_PRIMARY);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));

        statusLabel = new JLabel("Not clocked in today");
        statusLabel.setFont(UITheme.FONT_BODY_BOLD);
        statusLabel.setForeground(UITheme.DANGER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        buttonPanel.setBackground(Color.WHITE);

        timeInBtn = UITheme.createAccentButton("Time In");
        timeOutBtn = UITheme.createButton("Time Out");

        timeInBtn.setPreferredSize(new Dimension(120, 36));
        timeOutBtn.setPreferredSize(new Dimension(120, 36));

        timeInBtn.addActionListener(e -> {
            attendanceService.timeIn(employee.getEmployeeId());
            refreshAttendance();
        });

        timeOutBtn.addActionListener(e -> {
            attendanceService.timeOut(employee.getEmployeeId());
            refreshAttendance();
        });

        buttonPanel.add(timeInBtn);
        buttonPanel.add(timeOutBtn);

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setBackground(Color.WHITE);
        topSection.add(title, BorderLayout.NORTH);
        topSection.add(statusLabel, BorderLayout.CENTER);

        panel.add(topSection, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel outerWrapper = new JPanel(new BorderLayout());
        outerWrapper.setBackground(UITheme.BG);
        outerWrapper.setBorder(new EmptyBorder(0, 0, 16, 0));
        outerWrapper.add(panel, BorderLayout.CENTER);

        return outerWrapper;
    }

    private JScrollPane createHistoryPanel() {

        table = new JTable();
        UITheme.styleTable(table);

        JScrollPane scrollPane = UITheme.createTableScrollPane(table);
        refreshAttendance();

        return scrollPane;
    }

    private void refreshAttendance() {

        List<AttendanceRecord> history =
                attendanceService.getAttendanceHistory(employee.getEmployeeId());

        String[] columns = {"Date", "Time In", "Time Out", "Hours Worked"};
        Object[][] data = new Object[history.size()][4];

        for (int i = 0; i < history.size(); i++) {
            AttendanceRecord record = history.get(i);
            data[i][0] = record.getDate();
            data[i][1] = record.getTimeIn();
            data[i][2] = (record.getTimeOut() == null || record.getTimeOut().isEmpty())
                    ? "--"
                    : record.getTimeOut();
            data[i][3] = String.format("%.2f", calculateHoursWorked(record));
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model);

        if (history.isEmpty()) {
            statusLabel.setText("Not clocked in today");
            statusLabel.setForeground(UITheme.DANGER);
            timeInBtn.setEnabled(true);
            timeOutBtn.setEnabled(false);
            return;
        }

        AttendanceRecord latest = history.get(history.size() - 1);
        String todayStr = LocalDate.now().toString();

        if (latest.getDate().equals(todayStr)) {

            if (latest.getTimeIn() != null && !latest.getTimeIn().isEmpty()
                    && (latest.getTimeOut() == null || latest.getTimeOut().isEmpty())) {

                statusLabel.setText("Timed in today at " + latest.getTimeIn());
                statusLabel.setForeground(UITheme.SUCCESS);
                timeInBtn.setEnabled(false);
                timeOutBtn.setEnabled(true);

            } else if (latest.getTimeOut() != null && !latest.getTimeOut().isEmpty()) {

                statusLabel.setText("Done for today (" +
                        latest.getTimeIn() + " - " + latest.getTimeOut() + ")");
                statusLabel.setForeground(UITheme.TEXT_SECONDARY);
                timeInBtn.setEnabled(false);
                timeOutBtn.setEnabled(false);

            } else {
                statusLabel.setText("Not clocked in today");
                statusLabel.setForeground(UITheme.DANGER);
                timeInBtn.setEnabled(true);
                timeOutBtn.setEnabled(false);
            }

        } else {
            statusLabel.setText("Not clocked in today");
            statusLabel.setForeground(UITheme.DANGER);
            timeInBtn.setEnabled(true);
            timeOutBtn.setEnabled(false);
        }
    }

    private double calculateHoursWorked(AttendanceRecord record) {
        try {
            if (record.getTimeIn() == null || record.getTimeIn().isEmpty()
                    || record.getTimeOut() == null || record.getTimeOut().isEmpty()) {
                return 0.0;
            }

            LocalTime in = LocalTime.parse(record.getTimeIn());
            LocalTime out = LocalTime.parse(record.getTimeOut());

            long minutes = Duration.between(in, out).toMinutes();
            if (minutes < 0) {
                return 0.0;
            }

            return minutes / 60.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
}