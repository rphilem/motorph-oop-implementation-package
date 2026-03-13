package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        setBackground(UITheme.BG);

        add(UITheme.createTitleBar("Employee Management"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.BG);
        content.setBorder(new EmptyBorder(16, 20, 16, 20));

        content.add(createTable(), BorderLayout.CENTER);
        content.add(createButtonPanel(), BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);
    }

    private JScrollPane createTable() {

        table = new JTable();
        UITheme.styleTable(table);

        refreshTable();

        return UITheme.createTableScrollPane(table);
    }

    private void refreshTable() {

        List<Employee> list = service.getAllEmployees();

        String[] columns = {
                "ID", "First Name", "Last Name",
                "Position", "Status", "Basic Salary", "Role"
        };

        NumberFormat peso = NumberFormat.getCurrencyInstance(
                new Locale("en", "PH"));

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
                return false;
            }
        };

        table.setModel(model);
    }

    private JPanel createButtonPanel() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        panel.setBackground(UITheme.BG);

        JButton addBtn = UITheme.createButton("Add");
        JButton editBtn = UITheme.createButton("Edit");
        JButton deleteBtn = UITheme.createCrudDangerButton("Delete");

        addBtn.setPreferredSize(new Dimension(90, 34));
        editBtn.setPreferredSize(new Dimension(90, 34));
        deleteBtn.setPreferredSize(new Dimension(90, 34));

        addBtn.addActionListener(e -> openEmployeeFormDialog(null));
        editBtn.addActionListener(e -> editSelected());
        deleteBtn.addActionListener(e -> deleteSelected());

        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);

        return panel;
    }

    private void editSelected() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
        Employee emp = service.findById(id);
        openEmployeeFormDialog(emp);
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int id = Integer.parseInt(table.getValueAt(row, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete this employee?", "Confirm",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteEmployeeRecord(String.valueOf(id));
            refreshTable();
        }
    }

    private void openEmployeeFormDialog(Employee emp) {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        boolean isAdmin = false;
        EmployeeFormDialog dialog = new EmployeeFormDialog(
                parent, service, emp, isAdmin);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        refreshTable();
    }
}
