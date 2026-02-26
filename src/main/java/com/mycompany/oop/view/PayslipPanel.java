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
import java.awt.print.PrinterJob;
import java.awt.print.Printable;
import java.awt.Graphics2D;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.PayrollHistoryRecord;
import com.mycompany.oop.service.PayrollService;

public class PayslipPanel extends JPanel {

    private PayrollService payrollService;

    // ================= VALUE LABELS =================
    private JLabel basicValue;
    private JLabel allowanceValue;
    private JLabel grossValue;

    private JLabel sssValue;
    private JLabel philhealthValue;
    private JLabel pagibigValue;
    private JLabel taxValue;
    private JLabel totalValue;

    private JLabel netValue;

    public PayslipPanel(Employee employee){

        payrollService = new PayrollService();

        setLayout(new BorderLayout());
        setBackground(UITheme.MAIN_GRAY);

        add(UITheme.createTitleBar("My Payslip History"), BorderLayout.NORTH);

        JPanel content = UITheme.createInsetPanel();
        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        // ================= FETCH HISTORY =================
        List<PayrollHistoryRecord> history =
                payrollService.getPayrollHistoryForEmployee(
                        employee.getEmployeeId());

        JComboBox<String> cutoffBox = new JComboBox<>();
        for (PayrollHistoryRecord r : history) {
            cutoffBox.addItem(r.getCutoffPeriod());
        }

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(new JLabel("Select Cutoff: "));
        topPanel.add(cutoffBox);

        content.add(topPanel, BorderLayout.NORTH);

        // ================= BREAKDOWN PANEL =================
        JPanel breakdownPanel = new JPanel(new GridBagLayout());
        breakdownPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,20,8,20);
        gbc.anchor = GridBagConstraints.WEST;

        // Initialize value labels
        basicValue = createValueLabel();
        allowanceValue = createValueLabel();
        grossValue = createValueLabel();

        sssValue = createValueLabel();
        philhealthValue = createValueLabel();
        pagibigValue = createValueLabel();
        taxValue = createValueLabel();
        totalValue = createValueLabel();

        netValue = createValueLabel();

        // ================= EARNINGS =================
        addSectionTitle(breakdownPanel, gbc, 0, "EARNINGS");
        addRow(breakdownPanel, gbc, 1, "Basic Salary (Semi-Monthly)", basicValue);
        addRow(breakdownPanel, gbc, 2, "Allowance (Semi-Monthly)", allowanceValue);
        addRow(breakdownPanel, gbc, 3, "Gross Pay", grossValue);

        addDivider(breakdownPanel, gbc, 4);

        // ================= DEDUCTIONS =================
        addSectionTitle(breakdownPanel, gbc, 5, "DEDUCTIONS");
        addRow(breakdownPanel, gbc, 6, "SSS", sssValue);
        addRow(breakdownPanel, gbc, 7, "PhilHealth", philhealthValue);
        addRow(breakdownPanel, gbc, 8, "Pag-IBIG", pagibigValue);
        addRow(breakdownPanel, gbc, 9, "Withholding Tax", taxValue);
        addRow(breakdownPanel, gbc, 10, "Total Deductions", totalValue);

        addDivider(breakdownPanel, gbc, 11);

        // ================= NET PAY =================
        addSectionTitle(breakdownPanel, gbc, 12, "NET PAY");
        addRow(breakdownPanel, gbc, 13, "Net Salary", netValue);

        // ================= CONFIDENTIAL NOTICE =================
        JLabel confidentialLabel = new JLabel(
                "<html><center><i>CONFIDENTIAL: This document contains sensitive payroll information intended solely for the employee. Unauthorized disclosure is strictly prohibited.</i></center></html>"
        );
        confidentialLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
        confidentialLabel.setForeground(new Color(120,120,120));
        confidentialLabel.setHorizontalAlignment(SwingConstants.CENTER);
        confidentialLabel.setBorder(BorderFactory.createEmptyBorder(15,10,10,10));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(210,210,210)),
                        BorderFactory.createEmptyBorder(15,30,15,30)
                )
        );

        JLabel headerLabel = new JLabel("MotorPH Payroll Payslip", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10,10,20,10));

        wrapper.add(headerLabel, BorderLayout.NORTH);
        wrapper.add(breakdownPanel, BorderLayout.CENTER);
        wrapper.add(confidentialLabel, BorderLayout.SOUTH);

        content.add(wrapper, BorderLayout.CENTER);

        // ================= BUTTONS =================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        buttonPanel.setBackground(Color.WHITE);

        JButton printBtn = UITheme.createAccentButton("Print");
        JButton downloadBtn = UITheme.createButton("Download");

        printBtn.setPreferredSize(new Dimension(120,35));
        downloadBtn.setPreferredSize(new Dimension(120,35));

        printBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
        downloadBtn.setFont(new Font("Tahoma", Font.BOLD, 12));

        buttonPanel.add(downloadBtn);
        buttonPanel.add(printBtn);

        content.add(buttonPanel, BorderLayout.SOUTH);
        add(content, BorderLayout.CENTER);
        

        // ================= DROPDOWN ACTION =================
        NumberFormat peso =
                NumberFormat.getCurrencyInstance(new Locale("en","PH"));

        cutoffBox.addActionListener(e -> {

            int index = cutoffBox.getSelectedIndex();
            if(index >= 0){

                PayrollHistoryRecord record = history.get(index);

                basicValue.setText(peso.format(record.getBasicComponent()));
                allowanceValue.setText(peso.format(record.getAllowanceComponent()));
                grossValue.setText(peso.format(record.getGross()));

                sssValue.setText(peso.format(record.getSss()));
                philhealthValue.setText(peso.format(record.getPhilhealth()));
                pagibigValue.setText(peso.format(record.getPagibig()));
                taxValue.setText(peso.format(record.getTax()));
                totalValue.setText(peso.format(record.getTotalDeductions()));

                netValue.setText(peso.format(record.getNet()));
            }
        });

        if(!history.isEmpty()){
            cutoffBox.setSelectedIndex(0);
        }
    }

    // ================= ROW =================
    private void addRow(JPanel panel, GridBagConstraints gbc,
                        int y, String labelText, JLabel valueLabel) {

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(valueLabel, gbc);

        gbc.anchor = GridBagConstraints.WEST;
    }

    // ================= SECTION TITLE =================
    private void addSectionTitle(JPanel panel,
                                 GridBagConstraints gbc,
                                 int y,
                                 String title) {

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel section = new JLabel(title);
        section.setFont(new Font("Tahoma", Font.BOLD, 14));
        section.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));

        panel.add(section, gbc);

        gbc.gridwidth = 1;
    }

    // ================= DIVIDER =================
    private void addDivider(JPanel panel,
                            GridBagConstraints gbc,
                            int y) {

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10,25,10,25);

        panel.add(new JSeparator(), gbc);

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(8,20,8,20); // reset
    }
    
    // ================= VALUE LABEL CREATOR =================
    private JLabel createValueLabel(){
        JLabel lbl = new JLabel("₱ 0.00");
        lbl.setFont(new Font("Tahoma", Font.BOLD, 13));
        return lbl;
    }
}



/*
PAYSLIP PANEL – STRUCTURED PAYROLL VIEW

Purpose:
Displays detailed payroll breakdown for individual employee.

Enhancements:
Clear separation of Earnings, Deductions, and Net Pay
Allowance separated from deductions
Professional payroll structure layout
Semi-monthly breakdown visibility
Confidentiality notice added
Print and Download functionality included

Design Improvements:
No computation logic inside UI
Pulls data only from PayrollHistoryRecord
Easily extendable for:
Approval status
Finance sign-off
Electronic signature
Export to PDF

Architecture:
UI Layer → Service Layer → Repository Layer

Future Enhancements:
HR approval flow integration
Finance approval tagging
Payslip status tracking (Pending / Approved / Released)
Official PDF export instead of TXT
*/