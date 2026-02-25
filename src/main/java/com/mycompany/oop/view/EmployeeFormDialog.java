/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import javax.swing.*;
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
        setSize(500, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.MAIN_GRAY);

        add(UITheme.createTitleBar(getTitle()), BorderLayout.NORTH);

        JPanel content = UITheme.createInsetPanel();
        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JPanel formPanel = new JPanel(new GridLayout(0,2,10,10));
        formPanel.setBackground(Color.WHITE);

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
                    "Admin","HR","Finance","Employee","IT"
            });
            roleBox.setBorder(BorderFactory.createLoweredBevelBorder());
            formPanel.add(roleBox);
        }

        content.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(UITheme.MAIN_GRAY);

        JButton cancelBtn = UITheme.createButton("Cancel");
        JButton saveBtn = UITheme.createAccentButton("Save");

        cancelBtn.addActionListener(e -> dispose());
        saveBtn.addActionListener(e -> saveEmployee());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);

        content.add(buttonPanel, BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);

        if (employee != null) {
            populateFields(employee);
        }

    }

    // ================= FIELD BUILDER =================
    private JTextField createField(String labelText, JPanel panel) {

        panel.add(createLabel(labelText));

        JTextField field = new JTextField();
        styleField(field);
        panel.add(field);

        return field;
    }

    private JLabel createLabel(String text){
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Tahoma", Font.BOLD, 12));
        return lbl;
    }

    private void styleField(JTextField field){
        field.setBorder(BorderFactory.createLoweredBevelBorder());
        field.setBackground(Color.WHITE);
        field.setFont(new Font("Tahoma", Font.PLAIN, 12));
    }

    // ================= POPULATE =================
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

    // ================= SAVE =================
    private void saveEmployee() {

        try {

            Employee newEmployee = new RegularEmployee(
                    Integer.parseInt(idField.getText()),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    positionField.getText(),
                    statusField.getText(),
                    Double.parseDouble(basicSalaryField.getText()),
                    Double.parseDouble(allowanceField.getText()),
                    Double.parseDouble(hourlyRateField.getText()),
                    isAdmin ? usernameField.getText() :
                            existingEmployee.getUsername(),
                    isAdmin ? new String(passwordField.getPassword()) :
                            existingEmployee.getPassword(),
                    isAdmin ? roleBox.getSelectedItem().toString() :
                            existingEmployee.getRole()
            );

            if (existingEmployee == null) {
                service.addEmployee(newEmployee);
            } else {
                service.updateEmployee(newEmployee);
            }

            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid input values",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}