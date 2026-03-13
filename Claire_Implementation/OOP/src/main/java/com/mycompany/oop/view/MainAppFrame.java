package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.oop.model.Employee;

public class MainAppFrame extends JFrame {

    private Employee employee;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private List<JButton> navButtons = new ArrayList<>();
    private JButton activeButton;

    public MainAppFrame(Employee employee) {

        this.employee = employee;

        setTitle("MotorPH Payroll System");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.BG);

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBackground(UITheme.SIDEBAR_BG);
        sidebar.setLayout(new BorderLayout());

        // Brand area
        JPanel brandPanel = new JPanel();
        brandPanel.setBackground(UITheme.SIDEBAR_BG);
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBorder(new EmptyBorder(28, 22, 16, 22));

        JLabel logo = new JLabel("MotorPH");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logo.setForeground(UITheme.ACCENT);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel userLabel = new JLabel(
                employee.getFirstName() + " \u2022 " + employee.getRole());
        userLabel.setFont(UITheme.FONT_SMALL);
        userLabel.setForeground(UITheme.TEXT_SIDEBAR);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        brandPanel.add(logo);
        brandPanel.add(Box.createVerticalStrut(6));
        brandPanel.add(userLabel);

        sidebar.add(brandPanel, BorderLayout.NORTH);

        // Navigation
        JPanel navPanel = new JPanel();
        navPanel.setBackground(UITheme.SIDEBAR_BG);
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBorder(new EmptyBorder(8, 8, 12, 8));

        // Separator
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(35, 55, 95));
        sep.setBackground(UITheme.SIDEBAR_BG);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        navPanel.add(sep);
        navPanel.add(Box.createVerticalStrut(12));

        // ===== NAVIGATION BUTTONS =====
        String role = employee.getRole();

        addNavButton(navPanel, "Dashboard", "DASH");

        if (role.equals("Admin")) {
            addNavButton(navPanel, "Employees", "EMP");
            addNavButton(navPanel, "User Management", "IT");
            addNavButton(navPanel, "Payroll", "PAYROLL");
            addNavButton(navPanel, "Payroll History", "PAYROLL_HISTORY");
            addNavButton(navPanel, "Leave Review", "LEAVE");
        }

        if (role.equals("HR")) {
            addNavButton(navPanel, "Employees", "EMP");
            addNavButton(navPanel, "Payroll History", "PAYROLL_HISTORY");
            addNavButton(navPanel, "Leave Review", "LEAVE");
        }

        if (role.equals("Finance")) {
            addNavButton(navPanel, "Payroll", "PAYROLL");
            addNavButton(navPanel, "Payroll History", "PAYROLL_HISTORY");
            addNavButton(navPanel, "Payslip Generator", "FINANCE_PAYSLIP");
        }

        if (role.equals("Employee")) {
            addNavButton(navPanel, "My Profile", "PROFILE");
            addNavButton(navPanel, "Attendance", "ATTENDANCE");
            addNavButton(navPanel, "Payslip", "PAYSLIP");
            addNavButton(navPanel, "File Leave", "FILE");
        }

        if (role.equals("IT")) {
            addNavButton(navPanel, "User Management", "IT");
        }

        // Set Dashboard as active by default
        if (!navButtons.isEmpty()) {
            setActiveButton(navButtons.get(0));
        }

        navPanel.add(Box.createVerticalGlue());

        // ===== LOGOUT =====
        JButton logoutBtn = UITheme.createSidebarDangerButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });

        navPanel.add(logoutBtn);
        navPanel.add(Box.createVerticalStrut(12));

        sidebar.add(navPanel, BorderLayout.CENTER);
        add(sidebar, BorderLayout.WEST);

        // ===== CONTENT AREA =====
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UITheme.BG);

        contentPanel.add(new DashboardPanel(employee), "DASH");
        contentPanel.add(new HRPanel(), "EMP");
        contentPanel.add(new PayrollPanel(), "PAYROLL");
        contentPanel.add(new PayslipPanel(employee), "PAYSLIP");
        contentPanel.add(new LeavePanel(employee), "FILE");
        contentPanel.add(new LeaveReviewPanel(), "LEAVE");
        contentPanel.add(new EmployeePanel(employee), "PROFILE");
        contentPanel.add(new ITPanel(), "IT");
        boolean canManagePayroll = role.equals("Admin");
        contentPanel.add(new HRPayrollHistoryPanel(canManagePayroll), "PAYROLL_HISTORY");
        contentPanel.add(new AttendancePanel(employee), "ATTENDANCE");
        contentPanel.add(new FinancePayslipPanel(), "FINANCE_PAYSLIP");

        add(contentPanel, BorderLayout.CENTER);
    }

    private void addNavButton(JPanel panel, String text, String card) {
        JButton btn = UITheme.createSidebarButton(text);
        navButtons.add(btn);

        btn.addActionListener(e -> {
            cardLayout.show(contentPanel, card);
            setActiveButton(btn);
        });

        panel.add(btn);
        panel.add(Box.createVerticalStrut(4));
    }

    private void setActiveButton(JButton selected) {
        for (JButton btn : navButtons) {
            btn.putClientProperty("sidebar.active", false);
            btn.setBackground(UITheme.SIDEBAR_BG);
            btn.setForeground(UITheme.TEXT_SIDEBAR);
        }
        activeButton = selected;
        activeButton.putClientProperty("sidebar.active", true);
        activeButton.setBackground(UITheme.SIDEBAR_HOVER);
        activeButton.setForeground(Color.WHITE);
    }
}
