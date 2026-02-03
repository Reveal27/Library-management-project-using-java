package com.library.util;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class StyleManager {

    public static void initializeFlatLaf() {
        try {

            UIManager.setLookAndFeel(new FlatLightLaf());

            UIManager.put("Button.arc", 10);
            UIManager.put("TextComponent.arc", 10);
            UIManager.put("ProgressBar.arc", 10);
            UIManager.put("Component.focusWidth", 2);
            
            System.out.println("FlatLaf Look-and-Feel initialized successfully");
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Error setting FlatLaf Look-and-Feel: " + e.getMessage());
            e.printStackTrace();

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                System.err.println("Error setting system Look-and-Feel: " + ex.getMessage());
            }
        }
    }
    public static void applyFlatLaf() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            javax.swing.SwingUtilities.updateComponentTreeUI(
                java.awt.Window.getWindows()[0]
            );
        } catch (Exception e) {
            System.err.println("Error applying FlatLaf theme: " + e.getMessage());
            e.printStackTrace();
        }
    }
}






