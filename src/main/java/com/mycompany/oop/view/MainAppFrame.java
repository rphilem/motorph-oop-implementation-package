/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import javax.swing.*;
import java.awt.*;
import com.mycompany.oop.model.Employee;

public class MainAppFrame extends JFrame {

    private Employee employee;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainAppFrame(Employee employee){

        this.employee = employee;

        setTitle("MotorPH Payroll System");
        setSize(1200,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.MAIN_GRAY);
        getRootPane().setDefaultButton(null);

        // ===== TOP TITLE BAR =====
        add(UITheme.createTitleBar("MotorPH Payroll System"),
                BorderLayout.NORTH);

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBackground(new Color(205,205,205));
        sidebar.setLayout(new BorderLayout());

        // Logo / Header
        JLabel logo = new JLabel("MotorPH");
        logo.setFont(new Font("Tahoma", Font.BOLD, 18));
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setBorder(BorderFactory.createEmptyBorder(20,10,20,10));
        sidebar.add(logo, BorderLayout.NORTH);

        // Button Container
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(205,205,205));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,15,10,15));

        sidebar.add(buttonPanel, BorderLayout.CENTER);

        // ===== NAVIGATION BUTTONS =====

        buttonPanel.add(createSidebarButton("Dashboard","DASH"));
        buttonPanel.add(Box.createVerticalStrut(10));

        String role = employee.getRole();

        if(role.equals("Admin")){
            addNavButton(buttonPanel,"Employees","EMP");
            addNavButton(buttonPanel,"Payroll","PAYROLL");
            addNavButton(buttonPanel,"Leave Review","LEAVE");
        }

        if(role.equals("HR")){
            addNavButton(buttonPanel,"Employees","EMP");
            addNavButton(buttonPanel,"Leave Review","LEAVE");
        }

        if(role.equals("Finance")){
            addNavButton(buttonPanel,"Payroll","PAYROLL");
        }

        if(role.equals("Employee")){
            addNavButton(buttonPanel,"My Profile","PROFILE");
            addNavButton(buttonPanel,"File Leave","FILE");
        }

        if(role.equals("IT")){
            addNavButton(buttonPanel,"User Management","IT");
        }

        buttonPanel.add(Box.createVerticalGlue());

        // ===== LOGOUT BUTTON =====
        JButton logoutBtn = UITheme.createSidebarDangerButton("Logout");
        logoutBtn.addActionListener(e -> {

            dispose(); // close dashboard

            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true); // IMPORTANT so it wont automatically closes when logged out
        });

        buttonPanel.add(logoutBtn);
        buttonPanel.add(Box.createVerticalStrut(15));

        add(sidebar, BorderLayout.WEST);

        // ===== CONTENT AREA =====
        cardLayout = new CardLayout();
        contentPanel = UITheme.createInsetPanel();
        contentPanel.setLayout(cardLayout);

        contentPanel.add(new DashboardPanel(employee),"DASH");
        contentPanel.add(new HRPanel(),"EMP");
        contentPanel.add(new PayrollPanel(),"PAYROLL");
        contentPanel.add(new LeavePanel(employee),"FILE");
        contentPanel.add(new LeaveReviewPanel(),"LEAVE");
        contentPanel.add(new EmployeePanel(employee),"PROFILE");
        contentPanel.add(new ITPanel(),"IT");

        add(contentPanel,BorderLayout.CENTER);

    }

    private void addNavButton(JPanel panel, String text, String card){
        panel.add(createSidebarButton(text, card));
        panel.add(Box.createVerticalStrut(10));
    }

    private JButton createSidebarButton(String text, String card) {

        JButton btn = UITheme.createSidebarButton(text);

        btn.addActionListener(e -> cardLayout.show(contentPanel, card));

        return btn;
    }
}