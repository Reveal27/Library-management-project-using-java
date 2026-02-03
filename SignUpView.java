package com.library.view;

import com.library.model.Department;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SignUpView extends JDialog {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<Department> departmentCombo;
    private JButton signUpButton;
    private JButton googleSignUpButton;
    private JButton cancelButton;

    public SignUpView(JFrame parent) {
        super(parent, "New Member Registration", true);
        initializeComponents();
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        gbc.gridy = 1;
        mainPanel.add(Box.createVerticalStrut(20), gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(usernameField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        emailField = new JTextField(20);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(emailField, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(passwordField, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(confirmPasswordLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPasswordField.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(confirmPasswordField, gbc);

        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel departmentLabel = new JLabel("Department:");
        departmentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(departmentLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        departmentCombo = new JComboBox<>(Department.values());
        departmentCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        departmentCombo.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(departmentCombo, gbc);
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(Box.createVerticalStrut(20), gbc);

        gbc.gridy = 8;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);

        signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        signUpButton.setPreferredSize(new Dimension(100, 35));
        signUpButton.setBackground(new Color(70, 130, 180));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFocusPainted(false);
        signUpButton.setFocusPainted(false);
        buttonPanel.add(signUpButton);

        googleSignUpButton = new JButton("Sign up with Google");
        googleSignUpButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        googleSignUpButton.setPreferredSize(new Dimension(150, 30));
        googleSignUpButton.setBackground(Color.WHITE);
        googleSignUpButton.setForeground(new Color(100, 100, 100));
        googleSignUpButton.setFocusPainted(false);
        buttonPanel.add(googleSignUpButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setFocusPainted(false);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
    }

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getConfirmPassword() {
        return new String(confirmPasswordField.getPassword());
    }

    public Department getSelectedDepartment() {
        return (Department) departmentCombo.getSelectedItem();
    }

    public void setSignUpButtonListener(ActionListener listener) {
        signUpButton.addActionListener(listener);
    }

    public void setCancelButtonListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }

    public void setGoogleSignUpButtonListener(ActionListener listener) {
        googleSignUpButton.addActionListener(listener);
    }

    public void clearFields() {
        usernameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        departmentCombo.setSelectedIndex(0);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
