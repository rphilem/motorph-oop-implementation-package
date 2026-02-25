/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.Leave;
import com.mycompany.oop.service.LeaveService;

public class LeavePanel extends JPanel {

    private LeaveService service;
    private JTable table;
    private Employee employee;

    public LeavePanel(Employee employee) {

        this.employee = employee;
        this.service = new LeaveService();

        setLayout(new BorderLayout());
        setBackground(UITheme.MAIN_GRAY);

        add(UITheme.createTitleBar("File Leave Request"), BorderLayout.NORTH);

        JPanel content = UITheme.createInsetPanel();
        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        content.add(createFormPanel(), BorderLayout.NORTH);
        content.add(createTablePanel(), BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);
    }

    // ================= FORM =================
    private JPanel createFormPanel() {

        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBorder(BorderFactory.createRaisedBevelBorder());
        formWrapper.setBackground(Color.WHITE);
        formWrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(15,15,15,15)
        ));

        JPanel formPanel = new JPanel(new GridLayout(5,2,10,10));
        formPanel.setBackground(Color.WHITE);

        JComboBox<String> typeBox = new JComboBox<>(
                new String[]{"Vacation","Sick","Emergency"});

        JTextField startField = new JTextField();
        JTextField endField = new JTextField();
        JTextField reasonField = new JTextField();

        styleField(typeBox);
        styleField(startField);
        styleField(endField);
        styleField(reasonField);

        formPanel.add(createLabel("Leave Type:"));
        formPanel.add(typeBox);

        formPanel.add(createLabel("Start Date (YYYY-MM-DD):"));
        formPanel.add(startField);

        formPanel.add(createLabel("End Date (YYYY-MM-DD):"));
        formPanel.add(endField);

        formPanel.add(createLabel("Reason:"));
        formPanel.add(reasonField);

        JButton submitBtn = UITheme.createAccentButton("Submit Leave");
        submitBtn.setFocusable(false);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonRow.setBackground(Color.WHITE);
        buttonRow.add(submitBtn);

        formWrapper.add(formPanel, BorderLayout.CENTER);
        formWrapper.add(buttonRow, BorderLayout.SOUTH);

        // ACTION
        submitBtn.addActionListener(e -> {

            try {

                Leave leave = service.fileLeave(
                        employee.getEmployeeId(),
                        typeBox.getSelectedItem().toString(),
                        startField.getText(),
                        endField.getText(),
                        reasonField.getText()
                );

                JOptionPane.showMessageDialog(this,
                        "Leave Filed Successfully.\nStatus: " + leave.getStatus());

                refreshTable();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        });   // closes addActionListener

        return formWrapper;

    }

    // ================= TABLE =================
    private JScrollPane createTablePanel() {

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

    private void refreshTable() {

        List<Leave> leaves =
                service.getLeavesByEmployee(employee.getEmployeeId());

        String[] columns = {
                "Leave ID","Type","Start","End","Reason","Status"
        };

        Object[][] data = new Object[leaves.size()][6];

        for (int i = 0; i < leaves.size(); i++) {

            Leave l = leaves.get(i);

            data[i][0] = l.getLeaveId();
            data[i][1] = l.getLeaveType();
            data[i][2] = l.getStartDate();
            data[i][3] = l.getEndDate();
            data[i][4] = l.getReason();
            data[i][5] = l.getStatus();
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model);
    }

    // ================= STYLING HELPERS =================
    private JLabel createLabel(String text){
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Tahoma", Font.BOLD, 12));
        return lbl;
    }

    private void styleField(JComponent field){
        field.setFont(new Font("Tahoma", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createLoweredBevelBorder());
        field.setBackground(Color.WHITE);
    }
}
