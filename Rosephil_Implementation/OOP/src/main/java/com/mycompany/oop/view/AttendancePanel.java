/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import com.mycompany.oop.model.AttendanceRecord;
import com.mycompany.oop.model.Employee;
import com.mycompany.oop.service.AttendanceService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
        setBackground(UITheme.MAIN_GRAY);

        add(UITheme.createTitleBar("My Attendance"), BorderLayout.NORTH);

        JPanel content = UITheme.createInsetPanel();
        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

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
                        BorderFactory.createLineBorder(new Color(210,210,210)),
                        BorderFactory.createEmptyBorder(20,20,20,20)
                )
        );

        JLabel title = new JLabel("Today's Attendance");
        title.setFont(new Font("Tahoma", Font.BOLD, 16));

        statusLabel = new JLabel("Not clocked in today");
        statusLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        statusLabel.setForeground(new Color(170, 40, 40));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10,0,15,0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        timeInBtn = UITheme.createAccentButton("Time In");
        timeOutBtn = UITheme.createButton("Time Out");

        timeInBtn.setPreferredSize(new Dimension(120, 35));
        timeOutBtn.setPreferredSize(new Dimension(120, 35));

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

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBackground(Color.WHITE);

        top.add(title);
        top.add(statusLabel);
        top.add(buttonPanel);

        panel.add(top, BorderLayout.CENTER);

        return panel;
    }

    private JScrollPane createHistoryPanel() {

        table = new JTable();
        table.setRowHeight(26);
        table.setFont(new Font("Tahoma", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 12));
        table.setSelectionBackground(new Color(0,0,128));
        table.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));

        return scrollPane;
    }

    private void refreshAttendance() {

        List<AttendanceRecord> history =
                attendanceService.getAttendanceHistory(employee.getEmployeeId());

        String[] columns = {"Date", "Time In", "Time Out"};
        Object[][] data = new Object[history.size()][3];

        for (int i = 0; i < history.size(); i++) {
            AttendanceRecord record = history.get(i);
            data[i][0] = record.getDate();
            data[i][1] = record.getTimeIn();
            data[i][2] = record.getTimeOut();
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
            statusLabel.setForeground(new Color(170, 40, 40));
            timeInBtn.setEnabled(true);
            timeOutBtn.setEnabled(false);
            return;
        }

        AttendanceRecord latest = history.get(history.size() - 1);

        java.time.LocalDate today = java.time.LocalDate.now();
        String todayStr = today.toString();

        if (latest.getDate().equals(todayStr)) {

            if (latest.getTimeIn() != null && !latest.getTimeIn().isEmpty()
                    && (latest.getTimeOut() == null || latest.getTimeOut().isEmpty())) {

                statusLabel.setText("Timed in today at " + latest.getTimeIn());
                statusLabel.setForeground(new Color(30,120,30));
                timeInBtn.setEnabled(false);
                timeOutBtn.setEnabled(true);

            } else if (latest.getTimeOut() != null && !latest.getTimeOut().isEmpty()) {

                statusLabel.setText("Completed attendance for today");
                statusLabel.setForeground(new Color(30,120,30));
                timeInBtn.setEnabled(false);
                timeOutBtn.setEnabled(false);

            } else {
                statusLabel.setText("Not clocked in today");
                statusLabel.setForeground(new Color(170, 40, 40));
                timeInBtn.setEnabled(true);
                timeOutBtn.setEnabled(false);
            }

        } else {
            statusLabel.setText("Not clocked in today");
            statusLabel.setForeground(new Color(170, 40, 40));
            timeInBtn.setEnabled(true);
            timeOutBtn.setEnabled(false);
        }
    }
}
