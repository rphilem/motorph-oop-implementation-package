/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import javax.swing.*;
import java.awt.*;
import com.mycompany.oop.model.Employee;

public class GradientHeaderPanel extends JPanel {

    public GradientHeaderPanel(Employee employee) {

        setPreferredSize(new Dimension(1000, 40));
        setLayout(new BorderLayout());

        setBackground(new Color(0, 0, 128)); // Win95 navy

        JLabel title = new JLabel(
                "  MotorPH Payroll System"
        );
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Tahoma", Font.BOLD, 13));

        JLabel userInfo = new JLabel(
                employee.getFirstName() + " (" + employee.getRole() + ")  "
        );
        userInfo.setForeground(Color.WHITE);
        userInfo.setFont(new Font("Tahoma", Font.PLAIN, 12));
        userInfo.setHorizontalAlignment(SwingConstants.RIGHT);

        add(title, BorderLayout.WEST);
        add(userInfo, BorderLayout.EAST);
    }
}


