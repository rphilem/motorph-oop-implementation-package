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
import com.mycompany.oop.service.EmployeeService;

public class HRPanel extends JPanel {

    private EmployeeService service;
    private JTable table;

    public HRPanel() {

        service = new EmployeeService();

        setLayout(new BorderLayout());
        setBackground(UITheme.MAIN_GRAY);

        add(UITheme.createTitleBar("Employee Management"), BorderLayout.NORTH);

        JPanel content = UITheme.createInsetPanel();
        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        content.add(createTable(), BorderLayout.CENTER);
        content.add(createButtonPanel(), BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);
    }

    // ================= TABLE =================
    private JScrollPane createTable() {

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
                "ID","First Name","Last Name",
                "Position","Status","Basic Salary","Role"
        };

        NumberFormat peso = NumberFormat.getCurrencyInstance(
                new Locale("en","PH"));

        Object[][] data = new Object[list.size()][7];

        for (int i = 0; i < list.size(); i++) {

            Employee e = list.get(i);

            data[i][0] = e.getEmployeeId();
            data[i][1] = e.getFirstName();
            data[i][2] = e.getLastName();
            data[i][3] = e.getPosition();
            data[i][4] = e.getEmploymentStatus();
            data[i][5] = peso.format(e.getBasicSalary());
            data[i][6] = e.getRole();
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // prevent direct editing
            }
        };

        table.setModel(model);
    }

    // ================= BUTTONS =================
    private JPanel createButtonPanel() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        panel.setBackground(UITheme.MAIN_GRAY);

        JButton addBtn = UITheme.createButton("Add");
        JButton editBtn = UITheme.createButton("Edit");
        JButton deleteBtn = UITheme.createAccentButton("Delete");

        addBtn.setPreferredSize(new Dimension(90,32));
        editBtn.setPreferredSize(new Dimension(90,32));
        deleteBtn.setPreferredSize(new Dimension(90,32));

        addBtn.addActionListener(e -> openEmployeeFormDialog(null));
        editBtn.addActionListener(e -> editSelected());
        deleteBtn.addActionListener(e -> deleteSelected());

        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);

        return panel;
    }

    // ================= EDIT =================
    private void editSelected() {

        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = Integer.parseInt(
                table.getValueAt(row,0).toString());

        Employee emp = service.findById(id);

        openEmployeeFormDialog(emp);
    }

    // ================= DELETE =================
    private void deleteSelected() {

        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = Integer.parseInt(
                table.getValueAt(row,0).toString());

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete this employee?",
                "Confirm",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteEmployee(id);
            refreshTable();
        }
    }

    // ================= DIALOG =================
    private void openEmployeeFormDialog(Employee emp) {

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

        // HR should NOT edit username/password/role
        boolean isAdmin = false;

        EmployeeFormDialog dialog =
                new EmployeeFormDialog(
                        parent,
                        service,
                        emp,
                        isAdmin
                );

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        refreshTable();
    }
}