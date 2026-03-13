package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.service.DashboardService;
import com.mycompany.oop.model.DashboardSummary;

public class DashboardPanel extends JPanel {

    private Employee user;
    private JPanel cardContainer;
    private DashboardService dashboardService;

    private static final Color[] CARD_ACCENTS = {
            new Color(210, 43, 43),
            new Color(37, 99, 195),
            new Color(34, 160, 70),
            new Color(130, 80, 210),
            new Color(59, 130, 246),
            new Color(185, 30, 30),
    };

    public DashboardPanel(Employee user) {

        this.user = user;

        setLayout(new BorderLayout());
        setBackground(UITheme.BG);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        add(createHeader(), BorderLayout.NORTH);

        cardContainer = new JPanel();
        cardContainer.setBackground(UITheme.BG);
        cardContainer.setLayout(new GridLayout(0, 2, 20, 20));

        add(cardContainer, BorderLayout.CENTER);

        dashboardService = new DashboardService();
        loadRoleDashboard();
    }

    private JPanel createHeader() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(new EmptyBorder(0, 0, 28, 0));

        JLabel title = new JLabel("Dashboard");
        title.setFont(UITheme.FONT_PAGE_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);

        JLabel welcome = new JLabel(
                "Welcome back, " + user.getFirstName() +
                        " \u2022 " + user.getRole());
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcome.setForeground(UITheme.TEXT_SECONDARY);
        welcome.setBorder(new EmptyBorder(6, 0, 0, 0));

        panel.add(title, BorderLayout.NORTH);
        panel.add(welcome, BorderLayout.SOUTH);

        return panel;
    }

    private void loadRoleDashboard() {
        switch (user.getRole()) {
            case "IT": showITMetrics(); break;
            case "HR": showHRMetrics(); break;
            case "Finance": showFinanceMetrics(); break;
            case "Admin": showAdminMetrics(); break;
            default: showEmployeeMetrics();
        }
    }

    private void showITMetrics() {
        DashboardSummary s = dashboardService.generateSummary();
        addCard("Total Users", String.valueOf(s.getTotalEmployees()), 0);
        addCard("Admin Accounts", String.valueOf(s.getAdminCount()), 1);
        addCard("HR Accounts", String.valueOf(s.getHrCount()), 2);
        addCard("Finance Accounts", String.valueOf(s.getFinanceCount()), 3);
        addCard("Employee Accounts", String.valueOf(s.getEmployeeCount()), 4);
        addCard("IT Accounts", String.valueOf(s.getItCount()), 5);
    }

    private void showHRMetrics() {
        DashboardSummary s = dashboardService.generateSummary();
        addCard("Total Employees", String.valueOf(s.getTotalEmployees()), 0);
        addCard("Active Employees", String.valueOf(s.getActiveEmployees()), 2);
        addCard("Pending Leaves", String.valueOf(s.getPendingLeaves()), 3);
        addCard("Total Gross Payroll", formatCurrency(s.getTotalGross()), 0);
        addCard("Total Allowances", formatCurrency(s.getTotalAllowance()), 1);
    }

    private void showFinanceMetrics() {
        DashboardSummary s = dashboardService.generateSummary();
        addCard("Total Gross Payroll", formatCurrency(s.getTotalGross()), 0);
        addCard("Total Net Payroll", formatCurrency(s.getTotalNet()), 2);
        addCard("Total Deductions", formatCurrency(s.getTotalDeductions()), 4);
        addCard("Employees Paid", String.valueOf(s.getTotalEmployees()), 1);
    }

    private void showAdminMetrics() { showFinanceMetrics(); }

    private void showEmployeeMetrics() {
        DashboardSummary s = dashboardService.generateSummary();
        addCard("My Gross Salary", formatCurrency(user.getGrossSalary()), 0);
        addCard("My Net Salary", formatCurrency(user.getNetSalary()), 2);
        addCard("My Allowance", formatCurrency(user.getTotalAllowance()), 1);
        addCard("My Pending Leaves", String.valueOf(s.getPendingLeaves()), 3);
    }

    private void addCard(String title, String value, int accentIndex) {
        cardContainer.add(createCard(title, value,
                CARD_ACCENTS[accentIndex % CARD_ACCENTS.length]));
    }

    private JPanel createCard(String title, String value, Color accent) {

        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, 4, getHeight(), 2, 2);
                g2.dispose();
            }
        };
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1),
                new EmptyBorder(22, 24, 22, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UITheme.FONT_CARD_LABEL);
        lblTitle.setForeground(UITheme.TEXT_SECONDARY);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(UITheme.FONT_CARD_VALUE);
        lblValue.setForeground(UITheme.TEXT_PRIMARY);
        lblValue.setBorder(new EmptyBorder(8, 0, 0, 0));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }

    private String formatCurrency(double value) {
        NumberFormat peso = NumberFormat.getCurrencyInstance(
                new Locale("en", "PH"));
        return peso.format(value);
    }
}
