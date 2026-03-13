/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.RegularEmployee;
import com.mycompany.oop.service.EmployeeService;

public class EmployeeFormDialog extends JDialog {

    private JTextField idField, firstNameField, lastNameField, positionField;
    private JTextField statusField, basicSalaryField, allowanceField, hourlyRateField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    private EmployeeService service;
    private boolean isAdmin;
    private Employee existingEmployee;

    public EmployeeFormDialog(JFrame parent,
                              EmployeeService service,
                              Employee employee,
                              boolean isAdmin) {

        super(parent, true);
        this.service = service;
        this.isAdmin = isAdmin;
        this.existingEmployee = employee;

        setTitle(employee == null ? "Add Employee" : "Edit Employee");
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.BG);

        add(UITheme.createTitleBar(getTitle()), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.BG);
        content.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 8));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(16, 16, 16, 16)
        ));

        idField = createField("Employee ID:", formPanel);
        firstNameField = createField("First Name:", formPanel);
        lastNameField = createField("Last Name:", formPanel);
        positionField = createField("Position:", formPanel);
        statusField = createField("Employment Status:", formPanel);
        basicSalaryField = createField("Basic Salary:", formPanel);
        allowanceField = createField("Allowance:", formPanel);
        hourlyRateField = createField("Hourly Rate:", formPanel);

        if (isAdmin) {
            usernameField = createField("Username:", formPanel);

            formPanel.add(createLabel("Password:"));
            passwordField = new JPasswordField();
            styleField(passwordField);
            formPanel.add(passwordField);

            formPanel.add(createLabel("Role:"));
            roleBox = new JComboBox<>(new String[]{
                    "Admin", "HR", "Finance", "Employee", "IT"
            });
            roleBox.setFont(UITheme.FONT_BODY);
            roleBox.setBackground(Color.WHITE);
            formPanel.add(roleBox);
        }

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(UITheme.BG);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        content.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(UITheme.BG);

        JButton cancelBtn = UITheme.createFormButton("Cancel");
        JButton saveBtn = UITheme.createAccentButton("Save");

        cancelBtn.setPreferredSize(new Dimension(90, 34));
        saveBtn.setPreferredSize(new Dimension(90, 34));

        cancelBtn.addActionListener(e -> dispose());
        saveBtn.addActionListener(e -> saveEmployee());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);

        content.add(buttonPanel, BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);

        if (employee != null) {
            populateFields(employee);
            idField.setEditable(false);
            idField.setBackground(new Color(235, 235, 235));
        }

        setSize(620, 470);
        setLocationRelativeTo(parent);
    }

    private JTextField createField(String labelText, JPanel panel) {
        panel.add(createLabel(labelText));
        JTextField field = new JTextField();
        styleField(field);
        panel.add(field);
        return field;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UITheme.FONT_BODY_BOLD);
        lbl.setForeground(UITheme.TEXT_PRIMARY);
        return lbl;
    }

    private void styleField(JTextField field) {
        field.setPreferredSize(new Dimension(180, 30));
        field.setFont(UITheme.FONT_BODY);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(4, 8, 4, 8)
        ));
    }

    private void populateFields(Employee e) {
        idField.setText(String.valueOf(e.getEmployeeId()));
        firstNameField.setText(e.getFirstName());
        lastNameField.setText(e.getLastName());
        positionField.setText(e.getPosition());
        statusField.setText(e.getEmploymentStatus());
        basicSalaryField.setText(String.valueOf(e.getBasicSalary()));
        allowanceField.setText(String.valueOf(e.getAllowance()));
        hourlyRateField.setText(String.valueOf(e.getHourlyRate()));

        if (isAdmin) {
            usernameField.setText(e.getUsername());
            passwordField.setText(e.getPassword());
            roleBox.setSelectedItem(e.getRole());
        }
    }

    private void saveEmployee() {

        if (idField.getText().trim().isEmpty()
                || firstNameField.getText().trim().isEmpty()
                || lastNameField.getText().trim().isEmpty()
                || positionField.getText().trim().isEmpty()
                || statusField.getText().trim().isEmpty()
                || basicSalaryField.getText().trim().isEmpty()
                || allowanceField.getText().trim().isEmpty()
                || hourlyRateField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (isAdmin) {
            if (usernameField.getText().trim().isEmpty()
                    || new String(passwordField.getPassword()).trim().isEmpty()
                    || roleBox.getSelectedItem() == null) {

                JOptionPane.showMessageDialog(this,
                        "Please complete username, password, and role.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            int employeeId = Integer.parseInt(idField.getText().trim());

            if (existingEmployee == null && service.findById(employeeId) != null) {
                JOptionPane.showMessageDialog(this,
                        "Employee ID already exists.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            double basicSalary = Double.parseDouble(basicSalaryField.getText().trim());
            double allowance = Double.parseDouble(allowanceField.getText().trim());
            double hourlyRate = Double.parseDouble(hourlyRateField.getText().trim());

            if (basicSalary < 0 || allowance < 0 || hourlyRate < 0) {
                JOptionPane.showMessageDialog(this,
                        "Salary values cannot be negative.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String username;
            String password;
            String role;

            if (isAdmin) {
                username = usernameField.getText().trim();
                password = new String(passwordField.getPassword()).trim();
                role = roleBox.getSelectedItem().toString();
            } else if (existingEmployee != null) {
                username = existingEmployee.getUsername();
                password = existingEmployee.getPassword();
                role = existingEmployee.getRole();
            } else {
                String first = firstNameField.getText().trim().toLowerCase().replace(" ", "");
                String last = lastNameField.getText().trim().toLowerCase().replace(" ", "");

                String lastInitial = last.isEmpty() ? "x" : String.valueOf(last.charAt(0));
                username = first + "." + lastInitial;

                password = "1234";
                role = "Employee";
            }

            Employee newEmployee = new RegularEmployee(
                    employeeId,
                    firstNameField.getText().trim(),
                    lastNameField.getText().trim(),
                    positionField.getText().trim(),
                    statusField.getText().trim(),
                    basicSalary,
                    allowance,
                    hourlyRate,
                    username,
                    password,
                    role
            );

            if (existingEmployee == null) {
                service.addEmployee(newEmployee);
            } else {
                service.updateEmployee(newEmployee);
            }

            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Employee ID, Basic Salary, Allowance, and Hourly Rate must be valid numbers.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid input values.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}