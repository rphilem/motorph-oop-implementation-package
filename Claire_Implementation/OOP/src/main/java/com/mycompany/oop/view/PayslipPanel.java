package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.PayrollHistoryRecord;
import com.mycompany.oop.service.PayrollService;

public class PayslipPanel extends JPanel {

    private PayrollService payrollService;

    private JLabel basicValue, allowanceValue, grossValue;
    private JLabel sssValue, philhealthValue, pagibigValue, taxValue, totalValue;
    private JLabel netValue;

    public PayslipPanel(Employee employee) {

        payrollService = new PayrollService();

        setLayout(new BorderLayout());
        setBackground(UITheme.BG);

        add(UITheme.createTitleBar("My Payslip History"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.BG);
        content.setBorder(new EmptyBorder(20, 24, 20, 24));

        // ===== FETCH HISTORY =====
        List<PayrollHistoryRecord> history =
                payrollService.getPayrollHistoryForEmployee(
                        employee.getEmployeeId());

        JComboBox<String> cutoffBox = new JComboBox<>();
        for (PayrollHistoryRecord r : history) {
            cutoffBox.addItem(r.getCutoffPeriod());
        }

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(8, 12, 8, 12)
        ));

        JLabel selectLabel = new JLabel("Select Cutoff:");
        selectLabel.setFont(UITheme.FONT_BODY_BOLD);
        topPanel.add(selectLabel);
        topPanel.add(cutoffBox);

        content.add(topPanel, BorderLayout.NORTH);

        // ===== BREAKDOWN =====
        JPanel breakdownPanel = new JPanel(new GridBagLayout());
        breakdownPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 24, 8, 24);
        gbc.anchor = GridBagConstraints.WEST;

        basicValue = createValueLabel();
        allowanceValue = createValueLabel();
        grossValue = createValueLabel();
        sssValue = createValueLabel();
        philhealthValue = createValueLabel();
        pagibigValue = createValueLabel();
        taxValue = createValueLabel();
        totalValue = createValueLabel();
        netValue = createValueLabel();

        addSectionTitle(breakdownPanel, gbc, 0, "EARNINGS");
        addRow(breakdownPanel, gbc, 1, "Basic Salary (Semi-Monthly)", basicValue);
        addRow(breakdownPanel, gbc, 2, "Allowance (Semi-Monthly)", allowanceValue);
        addRow(breakdownPanel, gbc, 3, "Gross Pay", grossValue);

        addDivider(breakdownPanel, gbc, 4);

        addSectionTitle(breakdownPanel, gbc, 5, "DEDUCTIONS");
        addRow(breakdownPanel, gbc, 6, "SSS", sssValue);
        addRow(breakdownPanel, gbc, 7, "PhilHealth", philhealthValue);
        addRow(breakdownPanel, gbc, 8, "Pag-IBIG", pagibigValue);
        addRow(breakdownPanel, gbc, 9, "Withholding Tax", taxValue);
        addRow(breakdownPanel, gbc, 10, "Total Deductions", totalValue);

        addDivider(breakdownPanel, gbc, 11);

        addSectionTitle(breakdownPanel, gbc, 12, "NET PAY");
        addRow(breakdownPanel, gbc, 13, "Net Salary", netValue);

        // Confidential notice
        JLabel confidentialLabel = new JLabel(
                "<html><center><i>CONFIDENTIAL: This document contains sensitive payroll information intended solely for the employee.</i></center></html>"
        );
        confidentialLabel.setFont(UITheme.FONT_SMALL);
        confidentialLabel.setForeground(UITheme.TEXT_SECONDARY);
        confidentialLabel.setHorizontalAlignment(SwingConstants.CENTER);
        confidentialLabel.setBorder(new EmptyBorder(18, 10, 8, 10));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(16, 30, 16, 30)
        ));

        JLabel headerLabel = new JLabel("MotorPH Payroll Payslip",
                SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(UITheme.TEXT_PRIMARY);
        headerLabel.setBorder(new EmptyBorder(10, 10, 20, 10));

        wrapper.add(headerLabel, BorderLayout.NORTH);
        wrapper.add(breakdownPanel, BorderLayout.CENTER);
        wrapper.add(confidentialLabel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        content.add(scrollPane, BorderLayout.CENTER);

        // ===== BUTTONS =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        buttonPanel.setBackground(UITheme.BG);

        JButton viewBtn = UITheme.createAccentButton("View Payslip");
        viewBtn.setPreferredSize(new Dimension(140, 36));

        viewBtn.addActionListener(e -> {
            int index = cutoffBox.getSelectedIndex();
            if (index >= 0) {
                PayrollHistoryRecord record = history.get(index);
                String empName = employee.getFirstName() + " " + employee.getLastName();
                PayslipDialog dialog = new PayslipDialog(
                        SwingUtilities.getWindowAncestor(this), empName, record);
                dialog.setVisible(true);
            }
        });

        buttonPanel.add(viewBtn);
        content.add(buttonPanel, BorderLayout.SOUTH);
        add(content, BorderLayout.CENTER);

        // ===== DROPDOWN ACTION =====
        NumberFormat peso = NumberFormat.getCurrencyInstance(
                new Locale("en", "PH"));

        cutoffBox.addActionListener(e -> {
            int index = cutoffBox.getSelectedIndex();
            if (index >= 0) {
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

        if (!history.isEmpty()) {
            cutoffBox.setSelectedIndex(0);
        }
    }

    private void addRow(JPanel panel, GridBagConstraints gbc,
                        int y, String labelText, JLabel valueLabel) {
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(UITheme.FONT_BODY);
        lbl.setForeground(UITheme.TEXT_SECONDARY);
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(valueLabel, gbc);
        gbc.anchor = GridBagConstraints.WEST;
    }

    private void addSectionTitle(JPanel panel, GridBagConstraints gbc,
                                 int y, String title) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel section = new JLabel(title);
        section.setFont(UITheme.FONT_SECTION);
        section.setForeground(UITheme.ACCENT);
        section.setBorder(new EmptyBorder(8, 0, 4, 0));
        panel.add(section, gbc);

        gbc.gridwidth = 1;
    }

    private void addDivider(JPanel panel, GridBagConstraints gbc, int y) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 28, 10, 28);
        panel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(8, 24, 8, 24);
    }

    private JLabel createValueLabel() {
        JLabel lbl = new JLabel("\u20B1 0.00");
        lbl.setFont(UITheme.FONT_BODY_BOLD);
        lbl.setForeground(UITheme.TEXT_PRIMARY);
        return lbl;
    }
}
