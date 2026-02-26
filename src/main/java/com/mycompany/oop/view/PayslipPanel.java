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

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.PayrollHistoryRecord;
import com.mycompany.oop.service.PayrollService;

import java.awt.print.PrinterJob;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.Graphics2D;

public class PayslipPanel extends JPanel {

    private PayrollService payrollService;

    private JLabel grossValue;
    private JLabel sssValue;
    private JLabel philhealthValue;
    private JLabel pagibigValue;
    private JLabel taxValue;
    private JLabel totalValue;
    private JLabel netValue;

    public PayslipPanel(Employee employee){

        payrollService = new PayrollService();
        
        JLabel confidentialLabel = new JLabel(
            "<html><center><i>CONFIDENTIAL: This document contains sensitive payroll " +
            "information intended solely for the employee. " +
            "Unauthorized disclosure is strictly prohibited.</i></center></html>"
        );

        confidentialLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
        confidentialLabel.setForeground(new Color(120,120,120));
        confidentialLabel.setHorizontalAlignment(SwingConstants.CENTER);
        confidentialLabel.setBorder(
                BorderFactory.createEmptyBorder(15,10,10,10)
        );

        setLayout(new BorderLayout());
        setBackground(UITheme.MAIN_GRAY);

        add(UITheme.createTitleBar("My Payslip History"), BorderLayout.NORTH);

        JPanel content = UITheme.createInsetPanel();
        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        // ================= TOP CUTOFF SELECTOR =================

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
        
        // ================= DOCUMENT HEADER =================
        JLabel headerLabel = new JLabel("MotorPH Payroll Payslip", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10,10,20,10));
        
        breakdownPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,20,10,20);
        gbc.anchor = GridBagConstraints.WEST;

        grossValue = createValueLabel();
        sssValue = createValueLabel();
        philhealthValue = createValueLabel();
        pagibigValue = createValueLabel();
        taxValue = createValueLabel();
        totalValue = createValueLabel();
        netValue = createValueLabel();

        addRow(breakdownPanel, gbc, 0, "Gross Salary", grossValue);
        addRow(breakdownPanel, gbc, 1, "SSS", sssValue);
        addRow(breakdownPanel, gbc, 2, "PhilHealth", philhealthValue);
        addRow(breakdownPanel, gbc, 3, "Pag-IBIG", pagibigValue);
        addRow(breakdownPanel, gbc, 4, "Withholding Tax", taxValue);
        addRow(breakdownPanel, gbc, 5, "Total Deductions", totalValue);
        addRow(breakdownPanel, gbc, 6, "Net Salary", netValue);

        // Wrap breakdown + confidential notice together
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(210,210,210)),
                        BorderFactory.createEmptyBorder(20,30,20,30)
                )
        );

        wrapper.add(headerLabel, BorderLayout.NORTH);
        wrapper.add(breakdownPanel, BorderLayout.CENTER);
        wrapper.add(confidentialLabel, BorderLayout.SOUTH);

        // ================= ACTION BUTTONS =================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton printBtn = UITheme.createButton("Print");
        JButton downloadBtn = UITheme.createButton("Download");

        printBtn.setPreferredSize(new Dimension(100,30));
        downloadBtn.setPreferredSize(new Dimension(100,30));

        buttonPanel.add(downloadBtn);
        buttonPanel.add(printBtn);

        content.add(buttonPanel, BorderLayout.SOUTH);
        
        content.add(wrapper, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        // ================= DROPDOWN ACTION =================

        NumberFormat peso =
                NumberFormat.getCurrencyInstance(
                        new Locale("en","PH"));

        cutoffBox.addActionListener(e -> {

            int index = cutoffBox.getSelectedIndex();
            if(index >= 0){

                PayrollHistoryRecord record = history.get(index);

                grossValue.setText(peso.format(record.getGross()));
                sssValue.setText(peso.format(record.getSss()));
                philhealthValue.setText(peso.format(record.getPhilhealth()));
                pagibigValue.setText(peso.format(record.getPagibig()));
                taxValue.setText(peso.format(record.getTax()));
                totalValue.setText(peso.format(record.getTotalDeductions()));
                netValue.setText(peso.format(record.getNet()));
            }
        });
        
        printBtn.addActionListener(e -> {

            try {

                PrinterJob job = PrinterJob.getPrinterJob();

                job.setPrintable((graphics, pageFormat, pageIndex) -> {

                    if (pageIndex > 0) {
                        return Printable.NO_SUCH_PAGE;
                    }

                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.translate(
                            pageFormat.getImageableX(),
                            pageFormat.getImageableY()
                    );

                    wrapper.printAll(g2d);

                    return Printable.PAGE_EXISTS;
                });

                if (job.printDialog()) {
                    job.print();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });       
        
        downloadBtn.addActionListener(e -> {

            try {
                java.io.PrintWriter writer =
                        new java.io.PrintWriter("Payslip_" +
                                employee.getEmployeeId() + "_" +
                                cutoffBox.getSelectedItem() + ".txt");

                writer.println("MotorPH Payroll Payslip");
                writer.println("--------------------------------------");
                writer.println("Cutoff: " + cutoffBox.getSelectedItem());
                writer.println();
                writer.println("Gross Salary: " + grossValue.getText());
                writer.println("SSS: " + sssValue.getText());
                writer.println("PhilHealth: " + philhealthValue.getText());
                writer.println("Pag-IBIG: " + pagibigValue.getText());
                writer.println("Withholding Tax: " + taxValue.getText());
                writer.println("Total Deductions: " + totalValue.getText());
                writer.println("Net Salary: " + netValue.getText());
                writer.println();
                writer.println("CONFIDENTIAL: Unauthorized disclosure is prohibited.");

                writer.close();

                JOptionPane.showMessageDialog(this,
                        "Payslip downloaded successfully.");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });        

        if(history.size() > 0){
            cutoffBox.setSelectedIndex(0);
        }
    }

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

    private JLabel createValueLabel(){
        JLabel lbl = new JLabel("₱ 0.00");
        lbl.setFont(new Font("Tahoma", Font.BOLD, 14));
        return lbl;
    }
}



/*
PAYSLIP PANEL – EMPLOYEE SELF-SERVICE UPDATE SUMMARY

Purpose:
Allows employees to view their payroll history.

Enhancements:

Employee-Specific Filtering
Uses findByEmployeeId() from repository.

Cutoff Dropdown
Employee selects payroll period to view.

Detailed Breakdown Display:
Gross
SSS
PhilHealth
Pag-IBIG
Tax
Total Deductions
Net Salary

Real-Time Update on Selection
Dropdown selection updates breakdown panel.

Architecture:
Uses PayrollService as abstraction.
No direct file access.

Future Improvements:

Printable payslip format

PDF export

Payslip history table view

Employee payroll comparison

Design Principle:
Separation of concerns.
Employee sees only their own payroll data.

*/