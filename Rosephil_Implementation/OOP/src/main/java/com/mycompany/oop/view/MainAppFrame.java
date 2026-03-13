/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import javax.swing.*;
import java.awt.*;
import com.mycompany.oop.model.Employee;

public class MainAppFrame extends JFrame {

    private Employee employee;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    // Keep panel references so we can refresh specific tabs safely
    private HRPanel hrPanel;
    private ITPanel itPanel;
    private PayrollPanel payrollPanel;
    private PayslipPanel payslipPanel;
    private LeavePanel leavePanel;
    private LeaveReviewPanel leaveReviewPanel;
    private EmployeePanel employeePanel;
    private AttendancePanel attendancePanel;
    private HRPayrollHistoryPanel payrollHistoryPanel;
    private DashboardPanel dashboardPanel;

    public MainAppFrame(Employee employee) {

        this.employee = employee;

        setTitle("MotorPH Payroll System");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.MAIN_GRAY);
        getRootPane().setDefaultButton(null);

        // ===== TOP TITLE BAR =====
        add(UITheme.createTitleBar("MotorPH Payroll System"), BorderLayout.NORTH);

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBackground(new Color(205, 205, 205));
        sidebar.setLayout(new BorderLayout());

        JLabel logo = new JLabel("MotorPH");
        logo.setFont(new Font("Tahoma", Font.BOLD, 18));
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sidebar.add(logo, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(205, 205, 205));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        sidebar.add(buttonPanel, BorderLayout.CENTER);

        // ===== CONTENT AREA =====
        cardLayout = new CardLayout();
        contentPanel = UITheme.createInsetPanel();
        contentPanel.setLayout(cardLayout);

        dashboardPanel = new DashboardPanel(employee);
        hrPanel = new HRPanel();
        payrollPanel = new PayrollPanel();
        payslipPanel = new PayslipPanel(employee);
        leavePanel = new LeavePanel(employee);
        leaveReviewPanel = new LeaveReviewPanel();
        employeePanel = new EmployeePanel(employee);
        attendancePanel = new AttendancePanel(employee);
        itPanel = new ITPanel();
        payrollHistoryPanel = new HRPayrollHistoryPanel();

        contentPanel.add(dashboardPanel, "DASH");
        contentPanel.add(hrPanel, "EMP");
        contentPanel.add(payrollPanel, "PAYROLL");
        contentPanel.add(payslipPanel, "PAYSLIP");
        contentPanel.add(leavePanel, "FILE");
        contentPanel.add(leaveReviewPanel, "LEAVE");
        contentPanel.add(employeePanel, "PROFILE");
        contentPanel.add(attendancePanel, "ATTENDANCE");
        contentPanel.add(itPanel, "IT");
        contentPanel.add(payrollHistoryPanel, "PAYROLL_HISTORY");

        add(contentPanel, BorderLayout.CENTER);

        // ===== NAVIGATION BUTTONS =====
        String role = employee.getRole();

        if ("Admin".equals(role)) {
            addNavButton(buttonPanel, "Employees", "EMP");
            addNavButton(buttonPanel, "Payroll", "PAYROLL");
            addNavButton(buttonPanel, "Payroll History", "PAYROLL_HISTORY");
            addNavButton(buttonPanel, "Leave Review", "LEAVE");
            addNavButton(buttonPanel, "User Management", "IT");
        }

        if ("HR".equals(role)) {
            addNavButton(buttonPanel, "Employees", "EMP");
            addNavButton(buttonPanel, "Leave Review", "LEAVE");
        }

        if ("Finance".equals(role)) {
            addNavButton(buttonPanel, "Payroll", "PAYROLL");
            addNavButton(buttonPanel, "Payroll History", "PAYROLL_HISTORY");
        }

        if ("Employee".equals(role)) {
            addNavButton(buttonPanel, "My Profile", "PROFILE");
            addNavButton(buttonPanel, "Attendance", "ATTENDANCE");
            addNavButton(buttonPanel, "Payslip", "PAYSLIP");
            addNavButton(buttonPanel, "File Leave", "FILE");
        }

        if ("IT".equals(role)) {
            addNavButton(buttonPanel, "User Management", "IT");
        }

        buttonPanel.add(Box.createVerticalGlue());

        // ===== LOGOUT BUTTON =====
        JButton logoutBtn = UITheme.createSidebarDangerButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });

        buttonPanel.add(logoutBtn);
        buttonPanel.add(Box.createVerticalStrut(15));

        add(sidebar, BorderLayout.WEST);

        // Optional default view
        cardLayout.show(contentPanel, "DASH");
    }

    private void addNavButton(JPanel panel, String text, String card) {
        panel.add(createSidebarButton(text, card));
        panel.add(Box.createVerticalStrut(10));
    }

    private JButton createSidebarButton(String text, String card) {

        JButton btn = UITheme.createSidebarButton(text);

        btn.addActionListener(e -> {

            // Refresh panels that need updated CSV-backed data
            if ("EMP".equals(card)) {
                contentPanel.remove(hrPanel);
                hrPanel = new HRPanel();
                contentPanel.add(hrPanel, "EMP");
            }

            if ("IT".equals(card)) {
                contentPanel.remove(itPanel);
                itPanel = new ITPanel();
                contentPanel.add(itPanel, "IT");
            }

            if ("PAYROLL".equals(card)) {
                contentPanel.remove(payrollPanel);
                payrollPanel = new PayrollPanel();
                contentPanel.add(payrollPanel, "PAYROLL");
            }

            if ("PAYROLL_HISTORY".equals(card)) {
                contentPanel.remove(payrollHistoryPanel);
                payrollHistoryPanel = new HRPayrollHistoryPanel();
                contentPanel.add(payrollHistoryPanel, "PAYROLL_HISTORY");
            }

            if ("PROFILE".equals(card)) {
                contentPanel.remove(employeePanel);
                employeePanel = new EmployeePanel(employee);
                contentPanel.add(employeePanel, "PROFILE");
            }

            if ("ATTENDANCE".equals(card)) {
                contentPanel.remove(attendancePanel);
                attendancePanel = new AttendancePanel(employee);
                contentPanel.add(attendancePanel, "ATTENDANCE");
            }

            if ("PAYSLIP".equals(card)) {
                contentPanel.remove(payslipPanel);
                payslipPanel = new PayslipPanel(employee);
                contentPanel.add(payslipPanel, "PAYSLIP");
            }

            if ("FILE".equals(card)) {
                contentPanel.remove(leavePanel);
                leavePanel = new LeavePanel(employee);
                contentPanel.add(leavePanel, "FILE");
            }

            if ("LEAVE".equals(card)) {
                contentPanel.remove(leaveReviewPanel);
                leaveReviewPanel = new LeaveReviewPanel();
                contentPanel.add(leaveReviewPanel, "LEAVE");
            }

            contentPanel.revalidate();
            contentPanel.repaint();
            cardLayout.show(contentPanel, card);
        });

        return btn;
    }
}

/*
MAIN APPLICATION FRAME – SYSTEM DASHBOARD CONTROLLER

Purpose:
MainAppFrame serves as the primary container and navigation controller
for the MotorPH Payroll System after successful user login.

It manages the overall application layout including:
• Top title bar
• Sidebar navigation menu
• Dynamic content area using CardLayout

Core Responsibilities:
• Enforce Role Based Access Control (RBAC)
• Load system modules based on the logged-in user role
• Handle navigation between system panels
• Refresh selected panels when opened to reflect updated CSV data

User Role Navigation Mapping:

Admin (Full Access)
• Employee Management
• Payroll Processing
• Payroll History
• Leave Review
• User Management

HR
• Employee Management
• Leave Review

Finance
• Payroll Processing
• Payroll History

Employee
• My Profile
• Attendance
• Payslip History
• File Leave

IT
• User Management
• System Administration tools

Dynamic Panel Refresh Behavior:
Panels that depend on CSV-based data are rebuilt when the user navigates
to them so that the latest saved records are displayed immediately.

Examples:
• HRPanel reloads employee records after add/edit/delete operations
• ITPanel refreshes user management data after role or password updates
• Payroll panels reload updated payroll records
• AttendancePanel reloads attendance history after time in/time out

This avoids stale UI data without requiring an application restart.

Design Pattern Usage:

CardLayout Pattern
• Used to switch efficiently between modules
• Keeps only one panel visible at a time

Controller-Oriented UI Coordination
• MainAppFrame acts as the central navigation controller
• It decides which module is shown based on role and user actions

Separation of Concerns:
• Business rules are handled by Service classes
• CSV persistence is handled by Repository classes
• MainAppFrame is responsible only for UI layout and navigation

Why This Matters:
• Ensures strict RBAC enforcement
• Keeps the interface modular and organized
• Maintains consistency between UI and persisted data
• Supports future module expansion with minimal structural change

Scalability Considerations:
Potential future improvements may include:
• Event-driven panel refresh
• Lazy panel initialization
• Dynamic role configuration
• Multi-session user support
*/