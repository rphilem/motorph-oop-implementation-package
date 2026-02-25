/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import com.mycompany.oop.model.Employee;
import com.mycompany.oop.service.EmployeeService;

public class ITPanel extends JPanel {

    private EmployeeService service;
    private JTable table;

    public ITPanel() {

        service = new EmployeeService();

        setLayout(new BorderLayout());
        setBackground(UITheme.MAIN_GRAY);

        add(UITheme.createTitleBar("System Administration"), BorderLayout.NORTH);

        JPanel content = UITheme.createInsetPanel();
        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        content.add(createDashboardCards(), BorderLayout.NORTH);
        content.add(createUserTable(), BorderLayout.CENTER);
        content.add(createButtonPanel(), BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);
    }

    // ================= DASHBOARD METRICS =================
    private JPanel createDashboardCards() {

        JPanel panel = new JPanel(new GridLayout(2,3,15,15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,20,10));

        Map<String,Integer> counts = service.getUserRoleCounts();

        int total = service.getTotalUsers();
        int admin = counts.getOrDefault("Admin", 0);
        int hr = counts.getOrDefault("HR", 0);
        int finance = counts.getOrDefault("Finance", 0);
        int employee = counts.getOrDefault("Employee", 0);
        int it = counts.getOrDefault("IT", 0);

        panel.add(createCard("Total Users", total));
        panel.add(createCard("Admins", admin));
        panel.add(createCard("HR Users", hr));
        panel.add(createCard("Finance Users", finance));
        panel.add(createCard("Employees", employee));
        panel.add(createCard("IT Users", it));

        return panel;
    }

    private JPanel createCard(String title, int value) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createRaisedBevelBorder());

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10,5,5,5));

        JLabel valueLabel = new JLabel(String.valueOf(value), SwingConstants.CENTER);
        valueLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        valueLabel.setBorder(BorderFactory.createEmptyBorder(5,5,10,5));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    // ================= USER TABLE =================
    private JScrollPane createUserTable() {

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

        List<Employee> list = service.getAllEmployees();

        String[] columns = {
                "Employee ID",
                "Username",
                "Role",
                "Status"
        };

        Object[][] data = new Object[list.size()][4];

        for (int i = 0; i < list.size(); i++) {

            Employee e = list.get(i);

            data[i][0] = e.getEmployeeId();
            data[i][1] = e.getUsername();
            data[i][2] = e.getRole();
            data[i][3] = e.getEmploymentStatus();
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model);
    }

    // ================= BUTTONS =================
    private JPanel createButtonPanel() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        panel.setBackground(UITheme.MAIN_GRAY);

        JButton resetBtn = UITheme.createButton("Reset Password");
        JButton roleBtn = UITheme.createAccentButton("Change Role");

        resetBtn.setPreferredSize(new Dimension(130,32));
        roleBtn.setPreferredSize(new Dimension(120,32));

        resetBtn.addActionListener(e -> resetPassword());
        roleBtn.addActionListener(e -> changeRole());

        panel.add(resetBtn);
        panel.add(roleBtn);

        return panel;
    }

    private void resetPassword() {

        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = Integer.parseInt(table.getValueAt(row,0).toString());

        String newPass = JOptionPane.showInputDialog(
                this,
                "Enter new password:");

        if (newPass != null && !newPass.isEmpty()) {
            service.resetPassword(id, newPass);
            refreshTable();
        }
    }

    private void changeRole() {

        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = Integer.parseInt(table.getValueAt(row,0).toString());

        String newRole = (String) JOptionPane.showInputDialog(
                this,
                "Select new role:",
                "Change Role",
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"Admin","HR","Finance","Employee","IT"},
                "Employee"
        );

        if (newRole != null) {
            service.changeRole(id, newRole);
            refreshTable();
        }
    }
}
