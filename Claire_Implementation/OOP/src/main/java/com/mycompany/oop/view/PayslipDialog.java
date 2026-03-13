package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

import com.mycompany.oop.model.PayrollHistoryRecord;

public class PayslipDialog extends JDialog {

    public PayslipDialog(Window parent, String employeeName, PayrollHistoryRecord record) {

        super(parent, "Payslip - " + employeeName + " - " + record.getCutoffPeriod(),
                ModalityType.APPLICATION_MODAL);

        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        NumberFormat peso = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));

        // ===== HEADER =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(20, 24, 10, 24));

        JLabel headerLabel = new JLabel("MotorPH Payroll Payslip",
                SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(UITheme.TEXT_PRIMARY);

        JLabel subHeader = new JLabel(
                employeeName + "  \u2022  " + record.getCutoffPeriod(),
                SwingConstants.CENTER);
        subHeader.setFont(UITheme.FONT_BODY);
        subHeader.setForeground(UITheme.TEXT_SECONDARY);
        subHeader.setBorder(new EmptyBorder(4, 0, 12, 0));

        topPanel.add(headerLabel, BorderLayout.NORTH);
        topPanel.add(subHeader, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // ===== BREAKDOWN =====
        JPanel breakdownPanel = new JPanel(new GridBagLayout());
        breakdownPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 28, 6, 28);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        addSectionTitle(breakdownPanel, gbc, row++, "EARNINGS");
        addRow(breakdownPanel, gbc, row++, "Basic Salary (Semi-Monthly)",
                peso.format(record.getBasicComponent()));
        addRow(breakdownPanel, gbc, row++, "Allowance (Semi-Monthly)",
                peso.format(record.getAllowanceComponent()));
        addRow(breakdownPanel, gbc, row++, "Gross Pay",
                peso.format(record.getGross()));

        addDivider(breakdownPanel, gbc, row++);

        addSectionTitle(breakdownPanel, gbc, row++, "DEDUCTIONS");
        addRow(breakdownPanel, gbc, row++, "SSS", peso.format(record.getSss()));
        addRow(breakdownPanel, gbc, row++, "PhilHealth", peso.format(record.getPhilhealth()));
        addRow(breakdownPanel, gbc, row++, "Pag-IBIG", peso.format(record.getPagibig()));
        addRow(breakdownPanel, gbc, row++, "Withholding Tax", peso.format(record.getTax()));
        addRow(breakdownPanel, gbc, row++, "Total Deductions",
                peso.format(record.getTotalDeductions()));

        addDivider(breakdownPanel, gbc, row++);

        addSectionTitle(breakdownPanel, gbc, row++, "NET PAY");
        addRow(breakdownPanel, gbc, row++, "Net Salary", peso.format(record.getNet()));

        // Confidential notice
        JLabel confidential = new JLabel(
                "<html><center><i>CONFIDENTIAL: This document contains sensitive payroll information.</i></center></html>"
        );
        confidential.setFont(UITheme.FONT_SMALL);
        confidential.setForeground(UITheme.TEXT_SECONDARY);
        confidential.setHorizontalAlignment(SwingConstants.CENTER);
        confidential.setBorder(new EmptyBorder(16, 10, 6, 10));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 24, 10, 24),
                BorderFactory.createLineBorder(UITheme.BORDER)
        ));
        wrapper.add(breakdownPanel, BorderLayout.CENTER);
        wrapper.add(confidential, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);

        // ===== CLOSE BUTTON =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        buttonPanel.setBackground(Color.WHITE);

        JButton closeBtn = UITheme.createFormButton("Close");
        closeBtn.setPreferredSize(new Dimension(90, 34));
        closeBtn.addActionListener(e -> dispose());
        buttonPanel.add(closeBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        setSize(520, 560);
        setLocationRelativeTo(parent);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc,
                        int y, String labelText, String value) {
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(UITheme.FONT_BODY);
        lbl.setForeground(UITheme.TEXT_SECONDARY);
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel val = new JLabel(value);
        val.setFont(UITheme.FONT_BODY_BOLD);
        val.setForeground(UITheme.TEXT_PRIMARY);
        panel.add(val, gbc);

        gbc.anchor = GridBagConstraints.WEST;
    }

    private void addSectionTitle(JPanel panel, GridBagConstraints gbc,
                                 int y, String title) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel section = new JLabel(title);
        section.setFont(UITheme.FONT_SECTION);
        section.setForeground(UITheme.ACCENT);
        section.setBorder(new EmptyBorder(6, 0, 3, 0));
        panel.add(section, gbc);

        gbc.gridwidth = 1;
    }

    private void addDivider(JPanel panel, GridBagConstraints gbc, int y) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 28, 8, 28);
        panel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(6, 28, 6, 28);
    }
}
