/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.Leave;
import com.mycompany.oop.service.DashboardService;
import com.mycompany.oop.model.DashboardSummary;

public class DashboardPanel extends JPanel {

    private Employee user;
    private JPanel cardContainer;
    private DashboardService dashboardService;

    public DashboardPanel(Employee user) {

        this.user = user;

        setLayout(new BorderLayout());
        setBackground(UITheme.DASHBOARD_BG);
        setBorder(BorderFactory.createEmptyBorder(30,30,30,30));

        add(createHeader(), BorderLayout.NORTH);

        cardContainer = new JPanel();
        cardContainer.setBackground(UITheme.DASHBOARD_BG);
        cardContainer.setLayout(new GridLayout(0,2,25,25));

        add(cardContainer, BorderLayout.CENTER);

        dashboardService = new DashboardService();
        loadRoleDashboard();
    }

    // ================= HEADER =================
    private JPanel createHeader(){

       JPanel panel = new JPanel(new BorderLayout());
       panel.setBackground(UITheme.DASHBOARD_BG);
       panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

       JLabel title = new JLabel("Dashboard");
       title.setFont(new Font("Segoe UI", Font.BOLD, 28));
       title.setForeground(new Color(30,30,40));
       title.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

       JLabel welcome = new JLabel(
               "Welcome back, " +
               user.getFirstName() +
               " (" + user.getRole() + ")"
       );
       welcome.setFont(new Font("Segoe UI", Font.PLAIN, 15));
       welcome.setForeground(new Color(90,90,110));

       panel.add(title, BorderLayout.NORTH);
       panel.add(welcome, BorderLayout.SOUTH);

       return panel;
   }

    // ================= ROLE SWITCH =================
    private void loadRoleDashboard() {

        switch (user.getRole()) {

            case "IT":
                showITMetrics();
                break;

            case "HR":
                showHRMetrics();
                break;

            case "Finance":
                showFinanceMetrics();
                break;

            case "Admin":
                showAdminMetrics();
                break;

            default:
                showEmployeeMetrics();
        }
    }

    // ================= IT =================
    private void showITMetrics() {

        DashboardSummary summary = dashboardService.generateSummary();

        addCard("Total Users",
                String.valueOf(summary.getTotalEmployees()));

        addCard("Admin Accounts",
                String.valueOf(summary.getAdminCount()));

        addCard("HR Accounts",
                String.valueOf(summary.getHrCount()));

        addCard("Finance Accounts",
                String.valueOf(summary.getFinanceCount()));

        addCard("Employee Accounts",
                String.valueOf(summary.getEmployeeCount()));

        addCard("IT Accounts",
                String.valueOf(summary.getItCount()));
    }

    // ================= HR =================
    private void showHRMetrics() {

        DashboardSummary summary = dashboardService.generateSummary();

        addCard("Total Employees",
                String.valueOf(summary.getTotalEmployees()));

        addCard("Active Employees",
                String.valueOf(summary.getActiveEmployees()));

        addCard("Pending Leaves",
                String.valueOf(summary.getPendingLeaves()));

        addCard("Total Gross Payroll",
                formatCurrency(summary.getTotalGross()));

        addCard("Total Allowances",
                formatCurrency(summary.getTotalAllowance()));
    }
    // ================= FINANCE =================
    private void showFinanceMetrics() {

        DashboardSummary summary = dashboardService.generateSummary();

        addCard("Total Gross Payroll",
                formatCurrency(summary.getTotalGross()));

        addCard("Total Net Payroll",
                formatCurrency(summary.getTotalNet()));

        addCard("Total Deductions",
                formatCurrency(summary.getTotalDeductions()));

        addCard("Employees Paid",
                String.valueOf(summary.getTotalEmployees()));
    }

    // ================= ADMIN =================
    private void showAdminMetrics() {
        showFinanceMetrics();
    }

    // ================= EMPLOYEE =================
    private void showEmployeeMetrics() {

        DashboardSummary summary = dashboardService.generateSummary();

        addCard("My Gross Salary",
                formatCurrency(user.computeGrossSalary()));

        addCard("My Net Salary",
                formatCurrency(user.computeNetSalary()));

        addCard("My Allowance",
                formatCurrency(user.getAllowance()));

        addCard("My Pending Leaves",
                String.valueOf(summary.getPendingLeaves()));
    }

    // ================= CARD CREATION =================
    private void addCard(String title, String value){
        cardContainer.add(createCard(title,value));
    }

    private JPanel createCard(String title, String value){

        JPanel card = UITheme.createDashboardCard();

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN,14));
        lblTitle.setForeground(new Color(100,100,120));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD,24));
        lblValue.setForeground(new Color(30,30,40));

        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }

    private String formatCurrency(double value) {
        NumberFormat peso =
                NumberFormat.getCurrencyInstance(
                        new Locale("en","PH"));
        return peso.format(value);
    }
}