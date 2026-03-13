/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import javax.swing.*;
import java.awt.*;
import com.mycompany.oop.service.LoginService;
import com.mycompany.oop.model.Employee;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private LoginService loginService;

    public LoginFrame() {

        loginService = new LoginService();

        setTitle("MotorPH Payroll System");
        setSize(420,280);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.MAIN_GRAY);

        add(UITheme.createTitleBar("MotorPH Payroll System"), BorderLayout.NORTH);

        // ===== CENTER WRAPPER =====
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(UITheme.MAIN_GRAY);

        JPanel loginCard = UITheme.createInsetPanel();
        loginCard.setLayout(new GridBagLayout());
        loginCard.setPreferredSize(new Dimension(320,180));
        loginCard.setBorder(BorderFactory.createEmptyBorder(20,25,20,25));

        wrapper.add(loginCard);
        add(wrapper, BorderLayout.CENTER);

        // ===== GRID CONFIG =====
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        styleField(usernameField);
        styleField(passwordField);

        // USERNAME
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        loginCard.add(createLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginCard.add(usernameField, gbc);

        // PASSWORD
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        loginCard.add(createLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginCard.add(passwordField, gbc);

        // LOGIN BUTTON (full width clean)
        JButton loginBtn = UITheme.createAccentButton("Login");
        loginBtn.setPreferredSize(new Dimension(0,32));
        loginBtn.addActionListener(e -> login());

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginCard.add(loginBtn, gbc);

        getRootPane().setDefaultButton(loginBtn);

    }

    // ===== LABEL STYLE =====
    private JLabel createLabel(String text){
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Tahoma", Font.BOLD, 12));
        return lbl;
    }

    // ===== FIELD STYLE =====
    private void styleField(JTextField field){
        field.setFont(new Font("Tahoma", Font.PLAIN, 12));
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(new Color(170,170,170)));
    }

    // ===== LOGIN LOGIC =====
    private void login() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter username and password.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Employee emp = loginService.login(username, password);

        if (emp == null) {
            JOptionPane.showMessageDialog(this,
                    "Invalid credentials.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Success case only
        dispose();
        MainAppFrame frame = new MainAppFrame(emp);
        frame.setVisible(true);
    }
}