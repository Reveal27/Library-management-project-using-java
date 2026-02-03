package com.library.controller;

import com.library.dao.IUserDAO;
import com.library.model.Department;
import com.library.model.User;
import com.library.model.UserType;
import com.library.service.GoogleAuthService;
import com.library.view.LoginView;
import com.library.view.SignUpView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.SwingWorker;

public class SignUpController {
    private SignUpView signUpView;
    private IUserDAO userDAO;
    private LoginView loginView;
    private GoogleAuthService googleAuthService;

    public SignUpController(SignUpView signUpView, IUserDAO userDAO, LoginView loginView) {
        this.signUpView = signUpView;
        this.userDAO = userDAO;
        this.loginView = loginView;
        this.googleAuthService = new GoogleAuthService();
        setupListeners();
    }

    private void setupListeners() {
        signUpView.setSignUpButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSignUp();
            }
        });

        signUpView.setGoogleSignUpButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleGoogleSignUp();
            }
        });

        signUpView.setCancelButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUpView.dispose();
            }
        });
    }

    private void handleSignUp() {
        String username = signUpView.getUsername();
        String email = signUpView.getEmail();
        String password = signUpView.getPassword();
        String confirmPassword = signUpView.getConfirmPassword();
        Department department = signUpView.getSelectedDepartment();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            signUpView.showError("Please fill in all fields.");
            return;
        }

        if (!email.endsWith("@aau.edu.et")) {
            signUpView.showError("Please use your institutional email (must end in @aau.edu.et).");
            return;
        }

        if (!password.equals(confirmPassword)) {
            signUpView.showError("Passwords do not match.");
            return;
        }

        if (password.length() < 4) {
            signUpView.showError("Password must be at least 4 characters long.");
            return;
        }

        if (userDAO.getUserByUsername(username) != null) {
            signUpView.showError("Username already exists. Please choose a different username.");
            return;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setUserType(UserType.STUDENT);
        newUser.setDepartment(department);

        if (userDAO.createUser(newUser)) {
            signUpView.showSuccess("Account created successfully! Please login.");
            signUpView.clearFields();
            signUpView.dispose();
        } else {
            signUpView.showError("Failed to create account. Please try again.");
        }
    }

    private void handleGoogleSignUp() {

        new SwingWorker<Map<String, String>, Void>() {
            @Override
            protected Map<String, String> doInBackground() throws Exception {
                return googleAuthService.authenticate();
            }

            @Override
            protected void done() {
                try {
                    Map<String, String> userInfo = get();
                    if (userInfo != null && userInfo.containsKey("email")) {
                        String email = userInfo.get("email");
                        if (!email.endsWith("@aau.edu.et")) {
                            signUpView.showError("Google Sign Up rejected. Email must end in @aau.edu.et");
                            return;
                        }

                        String username = email.split("@")[0];

                        User user = userDAO.getUserByUsername(username);
                        if (user != null) {
                            signUpView.showSuccess("Account already exists for " + username + ". Please login.");
                            signUpView.dispose();
                        } else {

                            User newUser = new User();
                            newUser.setUsername(username);
                            newUser.setEmail(email);
                            newUser.setPassword("GOOGLE_AUTH_PLACEHOLDER");
                            newUser.setUserType(UserType.STUDENT);
                            newUser.setDepartment(Department.SOFTWARE);

                            if (userDAO.createUser(newUser)) {
                                signUpView.showSuccess(
                                        "Google Account linked! Username: " + username + ". Please login.");
                                signUpView.clearFields();
                                signUpView.dispose();
                            } else {
                                signUpView.showError("Failed to create account with Google.");
                            }
                        }
                    } else {
                        signUpView.showError("Google Sign Up failed or was cancelled.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    signUpView.showError("An error occurred during Google Sign Up.");
                }
            }
        }.execute();
    }
}
