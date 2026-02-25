/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class UITheme {

    /*
     ============================================================
     GLOBAL COLOR SETTINGS
     Change values here to affect the entire application theme.
     ============================================================
    */

    public static final Color MAIN_GRAY = new Color(210,210,210);
    private static final Color SIDEBAR_GRAY = new Color(205,205,205);
    private static final Color CARD_GRAY = new Color(240,240,240);
    private static final Color NAVY = new Color(25,45,120);
    
    public static final Color DASHBOARD_BG = new Color(246,247,250);

    /*
     Shadow colors used for classic subtle emboss effect.
     To increase depth: darken SHADOW_DARK and SHADOW_DEEP.
     To flatten look: bring values closer together.
    */
    private static final Color SHADOW_LIGHT = new Color(245,245,245);
    private static final Color SHADOW_SOFT = new Color(225,225,225);
    private static final Color SHADOW_DARK = new Color(180,180,180);
    private static final Color SHADOW_DEEP = new Color(160,160,160);


    /*
     ============================================================
     TITLE BAR
     ============================================================
    */
    public static JPanel createTitleBar(String title){

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(NAVY);
        panel.setPreferredSize(new Dimension(0,45));

        JLabel label = new JLabel(title);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Tahoma", Font.BOLD, 16));
        label.setBorder(BorderFactory.createEmptyBorder(0,15,0,0));

        panel.add(label, BorderLayout.WEST);

        return panel;
    }


    /*
     ============================================================
     DEFAULT BUTTON
     Used for normal actions like Save, Add, Cancel.
     ============================================================
    */
    public static JButton createButton(String text) {

        JButton btn = new JButton(text);

        btn.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn.setFocusPainted(false);

        btn.setBackground(new Color(220,220,220));
        btn.setForeground(Color.BLACK);

        btn.setMargin(new Insets(6,12,6,12));
        btn.setBorder(createSoftRaisedBorder());

        return btn;
    }


    /*
     ============================================================
     PRIMARY ACCENT BUTTON
     Used for Login or major actions.
     ============================================================
    */
    public static JButton createAccentButton(String text){

        JButton btn = new JButton(text);

        btn.setFont(new Font("Tahoma", Font.BOLD, 13));
        btn.setFocusPainted(false);

        btn.setBackground(new Color(60,80,170));
        btn.setForeground(Color.WHITE);

        btn.setMargin(new Insets(6,14,6,14));
        btn.setBorder(createSoftRaisedBorder());

        return btn;
    }


    /*
     ============================================================
     SIDEBAR NAVIGATION BUTTON
     Used for Dashboard, Employees, Payroll, etc.
     ============================================================
    */
    public static JButton createSidebarButton(String text){

        JButton btn = new JButton(text);

        btn.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);

        btn.setBackground(new Color(215,215,215));
        btn.setForeground(Color.BLACK);

        btn.setMargin(new Insets(6,14,6,14));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
        btn.setBorder(createSoftRaisedBorder());

        return btn;
    }


    /*
     ============================================================
     SIDEBAR DANGER BUTTON
     Used for Logout.
     ============================================================
    */
    public static JButton createSidebarDangerButton(String text){

        JButton btn = new JButton(text);

        btn.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);

        btn.setBackground(new Color(190,70,70));
        btn.setForeground(Color.WHITE);

        btn.setMargin(new Insets(6,14,6,14));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
        btn.setBorder(createSoftRaisedBorder());

        return btn;
    }


    /*
     ============================================================
     PANEL STYLES
     ============================================================
    */
    public static JPanel createRaisedPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(SIDEBAR_GRAY);
        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        return panel;
    }

    public static JPanel createInsetPanel(){
        JPanel panel = new JPanel();
        panel.setBackground(CARD_GRAY);
        panel.setBorder(BorderFactory.createLineBorder(new Color(180,180,180)));
        return panel;
    }


    /*
     ============================================================
     SOFT CLASSIC WINDOWS BEVEL
     Centralized border style used by all buttons.
     ============================================================
    */
    private static Border createSoftRaisedBorder() {
        return BorderFactory.createBevelBorder(
                BevelBorder.RAISED,
                SHADOW_LIGHT,
                SHADOW_SOFT,
                SHADOW_DARK,
                SHADOW_DEEP
        );
    }

    /*
     ============================================================
     DASHBOARD CARD
     
     ============================================================
    */
    
    public static JPanel createDashboardCard(){

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);

        panel.setBorder(BorderFactory.createLineBorder(
                new Color(225,225,235)
        ));

        return panel;
    }

}