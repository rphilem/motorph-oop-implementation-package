package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.PayrollRecord;
import com.mycompany.oop.service.PayrollService;

public class PayrollPanel extends JPanel {

    private PayrollService payrollService;
    private JTable table;
    private JComboBox<String> cutoffBox;
    private JPanel summaryWrapper;

    public PayrollPanel() {

        payrollService = new PayrollService();

        setLayout(new BorderLayout());
        setBackground(UITheme.BG);

        add(UITheme.createTitleBar("Payroll Processing Center"),
                BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.BG);
        content.setBorder(new EmptyBorder(16, 20, 16, 20));

        summaryWrapper = new JPanel(new BorderLayout());
        summaryWrapper.setBackground(UITheme.BG);

        content.add(summaryWrapper, BorderLayout.NORTH);
        content.add(createTablePanel(), BorderLayout.CENTER);
        content.add(createButtonPanel(), BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);

        // Load initial data for the first cutoff
        if (cutoffBox.getItemCount() > 0) {
            refreshAll();
        }
    }

    // ================= SUMMARY =================

    private JPanel createSummaryPanel(String cutoffPeriod) {

        var summary = payrollService.generatePayrollSummary(cutoffPeriod);

        NumberFormat peso = NumberFormat.getCurrencyInstance(
                new Locale("en", "PH"));

        Color[] accents = {
                new Color(210, 43, 43), new Color(37, 99, 195),
                new Color(34, 160, 70), new Color(59, 130, 246),
                new Color(185, 30, 30), new Color(37, 99, 195),
                new Color(130, 80, 210), new Color(210, 43, 43),
        };

        String[][] cards = {
                {"Total Gross", peso.format(summary.getTotalGross())},
                {"Total Deductions", peso.format(summary.getTotalDeductions())},
                {"Total Net Payroll", peso.format(summary.getTotalNet())},
                {"Employees Paid", String.valueOf(summary.getEmployeeCount())},
                {"Total SSS", peso.format(summary.getTotalSSS())},
                {"Total PhilHealth", peso.format(summary.getTotalPhilhealth())},
                {"Total Pag-IBIG", peso.format(summary.getTotalPagibig())},
                {"Total Tax", peso.format(summary.getTotalTax())},
        };

        JPanel panel = new JPanel(new GridLayout(2, 4, 14, 14));
        panel.setBackground(UITheme.BG);
        panel.setBorder(new EmptyBorder(0, 0, 16, 0));

        for (int i = 0; i < cards.length; i++) {
            panel.add(createMetricCard(cards[i][0], cards[i][1], accents[i]));
        }

        return panel;
    }

    private JPanel createMetricCard(String title, String value, Color accent) {

        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(accent);
                g2.fillRect(0, 0, getWidth(), 3);
                g2.dispose();
            }
        };
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1),
                new EmptyBorder(14, 16, 12, 16)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UITheme.FONT_CARD_LABEL);
        titleLabel.setForeground(UITheme.TEXT_SECONDARY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueLabel.setForeground(UITheme.TEXT_PRIMARY);
        valueLabel.setBorder(new EmptyBorder(4, 0, 0, 0));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    // ================= TABLE =================

    private JScrollPane createTablePanel() {

        table = new JTable();
        UITheme.styleTable(table);

        return UITheme.createTableScrollPane(table);
    }

    private void refreshTable(String cutoffPeriod) {

        List<Employee> list = payrollService.getEmployees();

        String[] cols = {"Name", "Hours", "Gross", "Deductions", "Net"};

        NumberFormat peso = NumberFormat.getCurrencyInstance(
                new Locale("en", "PH"));

        Object[][] data = new Object[list.size()][5];

        for (int i = 0; i < list.size(); i++) {

            Employee e = list.get(i);

            double hours = payrollService.getHoursForCutoff(
                    e.getEmployeeId(), cutoffPeriod);

            PayrollRecord record = payrollService
                    .processPayrollForEmployee(e, hours);

            data[i][0] = e.getFirstName() + " " + e.getLastName();
            data[i][1] = String.format("%.1f", hours);
            data[i][2] = peso.format(record.getGross());
            data[i][3] = peso.format(record.getTotalDeductions());
            data[i][4] = peso.format(record.getNet());
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model);
    }

    // ================= BUTTONS =================

    private JPanel createButtonPanel() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        panel.setBackground(UITheme.BG);

        // Populate cutoff dropdown from attendance data
        List<String> cutoffs = payrollService.getAvailableCutoffs();
        cutoffBox = new JComboBox<>(cutoffs.toArray(new String[0]));

        JButton processBtn = UITheme.createAccentButton("Process Payroll");
        JButton refreshBtn = UITheme.createButton("Refresh");

        processBtn.setPreferredSize(new Dimension(160, 34));
        refreshBtn.setPreferredSize(new Dimension(100, 34));

        processBtn.addActionListener(e -> {

            if (cutoffBox.getSelectedItem() == null) return;

            String cutoff = cutoffBox.getSelectedItem().toString();

            boolean success = payrollService.processAndSavePayroll(
                    cutoff, false);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Payroll processed successfully for " + cutoff + ".");
                refreshAll();
            } else {
                JOptionPane.showMessageDialog(this,
                        "This cutoff has already been processed.",
                        "Duplicate Cutoff",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        refreshBtn.addActionListener(e -> refreshAll());

        cutoffBox.addActionListener(e -> refreshAll());

        JLabel cutoffLabel = new JLabel("Cutoff:");
        cutoffLabel.setFont(UITheme.FONT_BODY);

        panel.add(cutoffLabel);
        panel.add(cutoffBox);
        panel.add(refreshBtn);
        panel.add(processBtn);

        return panel;
    }

    // ================= REFRESH ALL =================

    private void refreshAll() {

        if (cutoffBox.getSelectedItem() == null) return;

        String cutoff = cutoffBox.getSelectedItem().toString();

        refreshTable(cutoff);

        summaryWrapper.removeAll();
        summaryWrapper.add(createSummaryPanel(cutoff), BorderLayout.CENTER);
        summaryWrapper.revalidate();
        summaryWrapper.repaint();
    }
}
