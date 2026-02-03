package com.library.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton googleLoginButton;
    private JButton signUpButton;
    private JLabel welcomeLabel;
    private JPanel mainPanel;
    private JPanel formPanel;

    public LoginView() {
        initializeComponents();
        setupLayout();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Library Management System - Login");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 250));

        JPanel backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setBackground(new Color(70, 130, 180));
        backgroundPanel.setPreferredSize(new Dimension(450, 600));

        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        welcomeLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h1 style='color: white; font-size: 28px; margin-bottom: 15px;'>Welcome to </h1>" +
                "<h2 style='color: white; font-size: 28px; margin-bottom: 10px;'> Library</h2>" +
                "<h3 style='color: white; font-size: 20px; margin-bottom: 30px;'>Management System</h3>" +
                "</div></html>");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(50, 30, 50, 30);
        welcomePanel.add(welcomeLabel, gbc);

        backgroundPanel.add(welcomePanel, BorderLayout.CENTER);
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 40));

        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.insets = new Insets(10, 10, 10, 10);
        gbcForm.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("Login to Your Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        gbcForm.gridx = 0;
        gbcForm.gridy = 0;
        gbcForm.gridwidth = 2;
        gbcForm.anchor = GridBagConstraints.CENTER;
        formPanel.add(titleLabel, gbcForm);

        gbcForm.gridy = 1;
        gbcForm.gridwidth = 2;
        formPanel.add(Box.createVerticalStrut(30), gbcForm);

        gbcForm.gridwidth = 1;
        gbcForm.anchor = GridBagConstraints.WEST;
        gbcForm.gridy = 2;
        gbcForm.gridx = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(usernameLabel, gbcForm);

        gbcForm.gridx = 1;
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        gbcForm.weightx = 1.0;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(250, 35));
        formPanel.add(usernameField, gbcForm);

        gbcForm.gridy = 3;
        gbcForm.gridx = 0;
        gbcForm.fill = GridBagConstraints.NONE;
        gbcForm.weightx = 0;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(passwordLabel, gbcForm);

        gbcForm.gridx = 1;
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        gbcForm.weightx = 1.0;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(250, 35));
        formPanel.add(passwordField, gbcForm);

        gbcForm.gridy = 4;
        gbcForm.gridx = 0;
        gbcForm.gridwidth = 2;
        gbcForm.fill = GridBagConstraints.NONE;
        gbcForm.weightx = 0;
        gbcForm.anchor = GridBagConstraints.CENTER;
        formPanel.add(Box.createVerticalStrut(20), gbcForm);

        gbcForm.gridy = 5;
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(250, 40));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        formPanel.add(loginButton, gbcForm);

        gbcForm.gridy = 6;
        googleLoginButton = new JButton("Sign in with Google");
        googleLoginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        googleLoginButton.setPreferredSize(new Dimension(250, 40));
        googleLoginButton.setBackground(Color.WHITE);
        googleLoginButton.setForeground(new Color(100, 100, 100));
        googleLoginButton.setFocusPainted(false);
        formPanel.add(googleLoginButton, gbcForm);

        gbcForm.gridy = 7;
        formPanel.add(Box.createVerticalStrut(15), gbcForm);

        gbcForm.gridy = 8;
        JPanel signUpPanel = new JPanel(new FlowLayout());
        signUpPanel.setOpaque(false);
        JLabel signUpLabel = new JLabel("New Member?");
        signUpLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        signUpPanel.add(signUpLabel);

        signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        signUpButton.setBorderPainted(false);
        signUpButton.setContentAreaFilled(false);
        signUpButton.setForeground(new Color(70, 130, 180));
        signUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpPanel.add(signUpButton);

        formPanel.add(signUpPanel, gbcForm);

        mainPanel.add(backgroundPanel, BorderLayout.WEST);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void setupLayout() {
    }

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void setLoginButtonListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void setSignUpButtonListener(ActionListener listener) {
        signUpButton.addActionListener(listener);
    }

    public void setGoogleLoginButtonListener(ActionListener listener) {
        googleLoginButton.addActionListener(listener);
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
