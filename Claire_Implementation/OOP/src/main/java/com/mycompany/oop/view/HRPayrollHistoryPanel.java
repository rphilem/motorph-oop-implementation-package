package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.mycompany.oop.model.PayrollHistoryRecord;
import com.mycompany.oop.service.PayrollService;

public class HRPayrollHistoryPanel extends JPanel {

    private PayrollService payrollService;
    private JTable table;
    private boolean canManage;

    public HRPayrollHistoryPanel() {
        this(true);
    }

    public HRPayrollHistoryPanel(boolean canManage) {

        this.canManage = canManage;
        payrollService = new PayrollService();

        setLayout(new BorderLayout());
        setBackground(UITheme.BG);

        add(UITheme.createTitleBar("Payroll History (All Employees)"),
                BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.BG);
        content.setBorder(new EmptyBorder(16, 20, 16, 20));

        table = new JTable();
        UITheme.styleTable(table);

        refreshTable();

        content.add(UITheme.createTableScrollPane(table), BorderLayout.CENTER);

        if (canManage) {
            content.add(createControlPanel(), BorderLayout.SOUTH);
        }

        add(content, BorderLayout.CENTER);
    }

    private void refreshTable() {

        List<PayrollHistoryRecord> list =
                payrollService.getAllPayrollHistory();

        String[] cols = {
                "Emp ID", "Cutoff",
                "Gross", "SSS", "PhilHealth",
                "Pag-IBIG", "Tax",
                "Total Deductions", "Net"
        };

        NumberFormat peso = NumberFormat.getCurrencyInstance(
                new Locale("en", "PH"));

        Object[][] data = new Object[list.size()][9];

        for (int i = 0; i < list.size(); i++) {
            PayrollHistoryRecord r = list.get(i);
            data[i][0] = r.getEmployeeId();
            data[i][1] = r.getCutoffPeriod();
            data[i][2] = peso.format(r.getGross());
            data[i][3] = peso.format(r.getSss());
            data[i][4] = peso.format(r.getPhilhealth());
            data[i][5] = peso.format(r.getPagibig());
            data[i][6] = peso.format(r.getTax());
            data[i][7] = peso.format(r.getTotalDeductions());
            data[i][8] = peso.format(r.getNet());
        }

        table.setModel(new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        });
    }

    private JPanel createControlPanel() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        panel.setBackground(UITheme.BG);

        JButton clearBtn = UITheme.createCrudDangerButton("Clear Cutoff");
        clearBtn.setPreferredSize(new Dimension(140, 34));
        clearBtn.addActionListener(e -> clearSelectedCutoff());

        panel.add(clearBtn);
        return panel;
    }

    private void clearSelectedCutoff() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a payroll record first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cutoff = table.getValueAt(row, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to clear cutoff:\n" + cutoff + " ?",
                "Confirm Clear",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            payrollService.deleteCutoff(cutoff);
            JOptionPane.showMessageDialog(this,
                    "Cutoff cleared successfully.");
            refreshTable();
        }
    }
}
