package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.PayrollHistoryRecord;
import com.mycompany.oop.service.PayrollService;
import com.mycompany.oop.service.EmployeeService;

public class FinancePayslipPanel extends JPanel {

    private EmployeeService employeeService;
    private PayrollService payrollService;
    private JComboBox<String> employeeBox;
    private JComboBox<String> cutoffBox;
    private List<Employee> employees;
    private List<PayrollHistoryRecord> currentHistory;

    public FinancePayslipPanel() {

        employeeService = new EmployeeService();
        payrollService = new PayrollService();

        setLayout(new BorderLayout());
        setBackground(UITheme.BG);

        add(UITheme.createTitleBar("Payslip Generator (Finance)"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.BG);
        content.setBorder(new EmptyBorder(20, 24, 20, 24));

        // ===== SELECTION =====
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 10));
        selectionPanel.setBackground(Color.WHITE);
        selectionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(12, 14, 12, 14)
        ));

        employees = employeeService.getAllEmployees();

        employeeBox = new JComboBox<>();
        for (Employee e : employees) {
            employeeBox.addItem(e.getEmployeeId() + " - " +
                    e.getFirstName() + " " + e.getLastName());
        }

        cutoffBox = new JComboBox<>();

        JButton generateBtn = UITheme.createAccentButton("Generate Payslip");
        generateBtn.setPreferredSize(new Dimension(160, 34));

        JLabel empLabel = new JLabel("Employee:");
        empLabel.setFont(UITheme.FONT_BODY_BOLD);
        JLabel cutLabel = new JLabel("Cutoff:");
        cutLabel.setFont(UITheme.FONT_BODY_BOLD);

        selectionPanel.add(empLabel);
        selectionPanel.add(employeeBox);
        selectionPanel.add(cutLabel);
        selectionPanel.add(cutoffBox);
        selectionPanel.add(generateBtn);

        content.add(selectionPanel, BorderLayout.NORTH);

        // ===== INFO =====
        JLabel infoLabel = new JLabel(
                "Select an employee and cutoff period, then click Generate Payslip.",
                SwingConstants.CENTER);
        infoLabel.setFont(UITheme.FONT_BODY);
        infoLabel.setForeground(UITheme.TEXT_SECONDARY);
        content.add(infoLabel, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        // ===== ACTIONS =====
        employeeBox.addActionListener(e -> loadCutoffs());

        generateBtn.addActionListener(e -> {
            int empIndex = employeeBox.getSelectedIndex();
            int cutoffIndex = cutoffBox.getSelectedIndex();

            if (empIndex < 0 || cutoffIndex < 0 || currentHistory == null
                    || currentHistory.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please select an employee with payroll history.",
                        "No Data", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Employee emp = employees.get(empIndex);
            PayrollHistoryRecord record = currentHistory.get(cutoffIndex);
            String empName = emp.getFirstName() + " " + emp.getLastName();

            PayslipDialog dialog = new PayslipDialog(
                    SwingUtilities.getWindowAncestor(this), empName, record);
            dialog.setVisible(true);
        });

        if (!employees.isEmpty()) {
            employeeBox.setSelectedIndex(0);
        }
    }

    private void loadCutoffs() {
        cutoffBox.removeAllItems();
        int empIndex = employeeBox.getSelectedIndex();
        if (empIndex < 0) return;

        Employee emp = employees.get(empIndex);
        currentHistory = payrollService.getPayrollHistoryForEmployee(
                emp.getEmployeeId());

        for (PayrollHistoryRecord r : currentHistory) {
            cutoffBox.addItem(r.getCutoffPeriod());
        }
    }
}
