package com.library.main;
import javax.swing.SwingUtilities;
import com.library.controller.LoginController;
import com.library.util.DatabaseVerifier;
import com.library.util.StyleManager;
import com.library.view.LoginView;
public class LibraryMain {
    public static void main(String[] args) {
        DatabaseVerifier.verifyAndDisplayDatabase();
        StyleManager.initializeFlatLaf();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    LoginView loginView = new LoginView();
                    new LoginController(loginView);
                    loginView.setVisible(true);
                } catch (Exception e) {
                    System.err.println("Error starting application: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
}
