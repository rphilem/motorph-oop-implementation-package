package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.RegularEmployee;
import com.mycompany.oop.service.EmployeeService;
import com.mycompany.oop.service.EmployeeValidator;

public class EmployeeFormDialog extends JDialog {

    private JTextField idField, firstNameField, lastNameField;
    private JTextField birthdayField, addressField, phoneNumberField, emailField;
    private JTextField sssField, philhealthField, tinField, pagibigField;
    private JTextField positionField, statusField, supervisorField;
    private JTextField basicSalaryField, riceSubsidyField, phoneAllowanceField;
    private JTextField clothingAllowanceField, hourlyRateField;
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

        // ===== FORM =====
        JPanel formPanel = new JPanel(new GridLayout(0, 4, 10, 8));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(16, 16, 16, 16)
        ));

        // Personal Info
        idField = createField("Employee ID:", formPanel);
        lastNameField = createField("Last Name:", formPanel);
        firstNameField = createField("First Name:", formPanel);
        birthdayField = createField("Birthday:", formPanel);
        addressField = createField("Address:", formPanel);
        phoneNumberField = createField("Phone Number:", formPanel);
        emailField = createField("Email:", formPanel);

        // Government IDs
        sssField = createField("SSS #:", formPanel);
        philhealthField = createField("PhilHealth #:", formPanel);
        tinField = createField("TIN #:", formPanel);
        pagibigField = createField("Pag-IBIG #:", formPanel);

        // Employment
        statusField = createField("Status:", formPanel);
        positionField = createField("Position:", formPanel);
        supervisorField = createField("Supervisor:", formPanel);

        // Salary
        basicSalaryField = createField("Basic Salary:", formPanel);
        riceSubsidyField = createField("Rice Subsidy:", formPanel);
        phoneAllowanceField = createField("Phone Allowance:", formPanel);
        clothingAllowanceField = createField("Clothing Allowance:", formPanel);
        hourlyRateField = createField("Hourly Rate:", formPanel);

        // Auth fields (Admin/IT only)
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
            formPanel.add(roleBox);
        }

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        content.add(scrollPane, BorderLayout.CENTER);

        // ===== BUTTONS =====
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
        }

        setSize(720, 580);
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
        field.setPreferredSize(new Dimension(160, 30));
        field.setFont(UITheme.FONT_BODY);
        field.setBackground(Color.WHITE);
    }

    private void populateFields(Employee e) {
        idField.setText(String.valueOf(e.getEmployeeId()));
        idField.setEditable(false);
        firstNameField.setText(e.getFirstName());
        lastNameField.setText(e.getLastName());
        birthdayField.setText(e.getBirthday());
        addressField.setText(e.getAddress());
        phoneNumberField.setText(e.getPhoneNumber());
        emailField.setText(e.getEmail());
        sssField.setText(e.getSssNumber());
        philhealthField.setText(e.getPhilhealthNumber());
        tinField.setText(e.getTinNumber());
        pagibigField.setText(e.getPagibigNumber());
        statusField.setText(e.getEmploymentStatus());
        positionField.setText(e.getPosition());
        supervisorField.setText(e.getImmediateSupervisor());
        basicSalaryField.setText(String.valueOf(e.getBasicSalary()));
        riceSubsidyField.setText(String.valueOf(e.getRiceSubsidy()));
        phoneAllowanceField.setText(String.valueOf(e.getPhoneAllowance()));
        clothingAllowanceField.setText(String.valueOf(e.getClothingAllowance()));
        hourlyRateField.setText(String.valueOf(e.getHourlyRate()));

        if (isAdmin) {
            usernameField.setText(e.getUsername());
            passwordField.setText(e.getPassword());
            roleBox.setSelectedItem(e.getRole());
        }
    }

    private void saveEmployee() {

        try {
            Employee newEmployee = new RegularEmployee(
                    Integer.parseInt(idField.getText().trim()),
                    firstNameField.getText().trim(),
                    lastNameField.getText().trim(),
                    birthdayField.getText().trim(),
                    addressField.getText().trim(),
                    phoneNumberField.getText().trim(),
                    emailField.getText().trim(),
                    sssField.getText().trim(),
                    philhealthField.getText().trim(),
                    tinField.getText().trim(),
                    pagibigField.getText().trim(),
                    statusField.getText().trim(),
                    positionField.getText().trim(),
                    supervisorField.getText().trim(),
                    Double.parseDouble(basicSalaryField.getText().trim()),
                    Double.parseDouble(riceSubsidyField.getText().trim()),
                    Double.parseDouble(phoneAllowanceField.getText().trim()),
                    Double.parseDouble(clothingAllowanceField.getText().trim()),
                    Double.parseDouble(hourlyRateField.getText().trim()),
                    isAdmin ? usernameField.getText().trim() :
                            existingEmployee != null ? existingEmployee.getUsername() : "",
                    isAdmin ? new String(passwordField.getPassword()) :
                            existingEmployee != null ? existingEmployee.getPassword() : "",
                    isAdmin ? roleBox.getSelectedItem().toString() :
                            existingEmployee != null ? existingEmployee.getRole() : "Employee"
            );

            EmployeeValidator validator = new EmployeeValidator();
            boolean isNew = (existingEmployee == null);
            List<String> errors = validator.validate(
                    newEmployee, service.getAllEmployees(), isNew);

            if (!errors.isEmpty()) {
                String message = String.join("\n", errors);
                JOptionPane.showMessageDialog(this,
                        message, "Validation Errors",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (isNew) {
                service.addEmployeeRecord(newEmployee);
            } else {
                service.updateEmployeeRecord(newEmployee);
            }

            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Salary, allowance, and hourly rate must be valid numbers.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving employee: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
