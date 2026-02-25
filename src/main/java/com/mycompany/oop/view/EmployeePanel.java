/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import com.mycompany.oop.model.Employee;

public class EmployeePanel extends JPanel {

    private Employee employee;

    public EmployeePanel(Employee employee) {

        this.employee = employee;

        setLayout(new BorderLayout());
        setBackground(UITheme.MAIN_GRAY);

        add(UITheme.createTitleBar("My Profile"), BorderLayout.NORTH);

        JPanel content = UITheme.createInsetPanel();
        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        NumberFormat peso = NumberFormat.getCurrencyInstance(
                new Locale("en","PH"));

        // ===== PERSONAL INFO SECTION =====
        JPanel personalSection = createSection("Personal Information");

        personalSection.add(createRow("Employee ID:",
                String.valueOf(employee.getEmployeeId())));
        personalSection.add(createRow("Name:",
                employee.getFirstName() + " " + employee.getLastName()));
        personalSection.add(createRow("Position:",
                employee.getPosition()));
        personalSection.add(createRow("Status:",
                employee.getEmploymentStatus()));
        personalSection.add(createRow("Role:",
                employee.getRole()));

        // ===== SALARY SECTION =====
        JPanel salarySection = createSection("Salary Details");

        salarySection.add(createRow("Basic Salary:",
                peso.format(employee.getBasicSalary())));
        salarySection.add(createRow("Allowance:",
                peso.format(employee.getAllowance())));
        salarySection.add(createRow("Gross Salary:",
                peso.format(employee.computeGrossSalary())));
        salarySection.add(createRow("Deductions:",
                peso.format(employee.computeDeductions())));
        salarySection.add(createRow("Net Salary:",
                peso.format(employee.computeNetSalary())));

        mainPanel.add(personalSection);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(salarySection);

        content.add(mainPanel, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
    }

    // ================= SECTION BUILDER =================
    private JPanel createSection(String title) {

        JPanel section = new JPanel(new BorderLayout());
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(10,10,10,10)
        ));
        section.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

        JPanel body = new JPanel();
        body.setLayout(new GridLayout(0,1,5,5));
        body.setBackground(Color.WHITE);

        section.add(lblTitle, BorderLayout.NORTH);
        section.add(body, BorderLayout.CENTER);

        return section;
    }

    // ================= ROW BUILDER =================
    private JPanel createRow(String label, String value){

        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Tahoma", Font.PLAIN, 12));

        JLabel val = new JLabel(value);
        val.setFont(new Font("Tahoma", Font.BOLD, 12));

        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.EAST);

        return row;
    }
}
