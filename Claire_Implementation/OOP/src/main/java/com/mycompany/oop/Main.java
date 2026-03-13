package com.mycompany.oop;

import javax.swing.SwingUtilities;
import com.mycompany.oop.view.UITheme;
import com.mycompany.oop.view.LoginFrame;

public class Main {

    public static void main(String[] args) {

        UITheme.initTheme();

        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}
