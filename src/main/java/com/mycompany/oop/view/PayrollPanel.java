/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import com.mycompany.oop.model.Employee;
import com.mycompany.oop.service.PayrollProcessor;
import com.mycompany.oop.service.PayrollService;



public class PayrollPanel extends JPanel {

    private PayrollService payrollService;
    private JTable table;

    public PayrollPanel(){

        payrollService = new PayrollService();

        setLayout(new BorderLayout());
        setBackground(UITheme.MAIN_GRAY);

        add(UITheme.createTitleBar("Payroll Processing Center"), BorderLayout.NORTH);

        JPanel content = UITheme.createInsetPanel();
        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        content.add(createSummaryPanel(), BorderLayout.NORTH);
        content.add(createTablePanel(), BorderLayout.CENTER);
        content.add(createButtonPanel(), BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);
    }

    // ================= SUMMARY CARDS =================
    private JPanel createSummaryPanel(){

       var summary = payrollService.generatePayrollSummary();

       NumberFormat peso = NumberFormat.getCurrencyInstance(
               new Locale("en","PH"));

       JPanel panel = new JPanel(new GridLayout(1,4,15,15));
       panel.setBackground(Color.WHITE);
       panel.setBorder(BorderFactory.createEmptyBorder(10,10,20,10));

       panel.add(createCard("Total Gross",
               peso.format(summary.getTotalGross())));

       panel.add(createCard("Total Deductions",
               peso.format(summary.getTotalDeductions())));

       panel.add(createCard("Total Net Payroll",
               peso.format(summary.getTotalNet())));

       panel.add(createCard("Employees Paid",
               String.valueOf(summary.getEmployeeCount())));

       return panel;
   }

    private JPanel createCard(String title, String value){

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createRaisedBevelBorder());

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10,5,5,5));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        valueLabel.setBorder(BorderFactory.createEmptyBorder(5,5,10,5));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    // ================= TABLE =================
    private JScrollPane createTablePanel(){

        table = new JTable();
        table.setRowHeight(26);
        table.setFont(new Font("Tahoma", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 12));
        table.setSelectionBackground(new Color(0,0,128));
        table.setSelectionForeground(Color.WHITE);

        refreshTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLoweredBevelBorder());

        return scroll;
    }

private void refreshTable(){

    List<Employee> list = payrollService.getEmployees();

    String[] cols = {"Name","Gross","Deductions","Net"};

    NumberFormat peso = NumberFormat.getCurrencyInstance(
            new Locale("en","PH"));

    Object[][] data = new Object[list.size()][4];

    PayrollProcessor processor = new PayrollProcessor();

    for(int i = 0; i < list.size(); i++){

        Employee e = list.get(i);

        data[i][0] = e.getFirstName() + " " + e.getLastName();
        data[i][1] = peso.format(processor.computeGross(e));
        data[i][2] = peso.format(processor.computeDeductions(e));
        data[i][3] = peso.format(processor.computeNet(e));
    }

    DefaultTableModel model = new DefaultTableModel(data, cols){
        @Override
        public boolean isCellEditable(int row, int column){
            return false;
        }
    };

    table.setModel(model);
}

    // ================= BUTTONS =================
    private JPanel createButtonPanel(){

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        panel.setBackground(UITheme.MAIN_GRAY);

        JButton processBtn = UITheme.createAccentButton("Process Payroll");
        JButton refreshBtn = UITheme.createButton("Refresh");

        processBtn.setPreferredSize(new Dimension(140,32));
        refreshBtn.setPreferredSize(new Dimension(100,32));

        processBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Payroll processed successfully.")
        );

        refreshBtn.addActionListener(e -> refreshTable());

        panel.add(refreshBtn);
        panel.add(processBtn);

        return panel;
    }
}