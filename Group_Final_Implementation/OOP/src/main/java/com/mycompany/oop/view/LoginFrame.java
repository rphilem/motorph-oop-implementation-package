/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        setSize(460, 440);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(UITheme.BG);

        setLayout(new GridBagLayout());

        JPanel loginCard = new JPanel(new GridBagLayout());
        loginCard.setBackground(Color.WHITE);
        loginCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(36, 44, 32, 44)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 2, 0);
        JLabel brandLabel = new JLabel("MotorPH", SwingConstants.CENTER);
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        brandLabel.setForeground(UITheme.ACCENT);
        loginCard.add(brandLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 28, 0);
        JLabel subtitleLabel = new JLabel("Payroll System", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(UITheme.TEXT_SECONDARY);
        loginCard.add(subtitleLabel, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 6, 0);
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(UITheme.FONT_BODY_BOLD);
        userLabel.setForeground(UITheme.TEXT_PRIMARY);
        loginCard.add(userLabel, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 16, 0);
        usernameField = new JTextField();
        usernameField.setFont(UITheme.FONT_BODY);
        usernameField.setPreferredSize(new Dimension(280, 36));
        loginCard.add(usernameField, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 6, 0);
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(UITheme.FONT_BODY_BOLD);
        passLabel.setForeground(UITheme.TEXT_PRIMARY);
        loginCard.add(passLabel, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 24, 0);
        passwordField = new JPasswordField();
        passwordField.setFont(UITheme.FONT_BODY);
        passwordField.setPreferredSize(new Dimension(280, 36));
        loginCard.add(passwordField, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 0, 0);
        JButton loginBtn = UITheme.createAccentButton("Sign In");
        loginBtn.setPreferredSize(new Dimension(280, 40));
        loginBtn.addActionListener(e -> login());
        loginCard.add(loginBtn, gbc);

        add(loginCard);

        getRootPane().setDefaultButton(loginBtn);
    }

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

        dispose();
        MainAppFrame frame = new MainAppFrame(emp);
        frame.setVisible(true);
    }
}