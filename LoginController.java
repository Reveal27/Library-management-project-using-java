package com.library.controller;

import com.library.dao.IUserDAO;
import com.library.dao.UserDAO;
import com.library.model.User;
import com.library.model.UserType;
import com.library.service.GoogleAuthService;
import com.library.view.LoginView;
import com.library.view.SignUpView;
import com.library.view.StudentDashboard;
import com.library.view.ManagerDashboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.SwingWorker;

public class LoginController {
    private LoginView loginView;
    private IUserDAO userDAO;
    private GoogleAuthService googleAuthService;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        this.userDAO = new UserDAO();
        this.googleAuthService = new GoogleAuthService();
        setupListeners();
    }

    private void setupListeners() {
        loginView.setLoginButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        loginView.setSignUpButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSignUp();
            }
        });

        loginView.setGoogleLoginButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleGoogleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = loginView.getUsername();
        String password = loginView.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            loginView.showError("Please enter both username and password.");
            return;
        }

        User user = userDAO.authenticate(username, password);
        if (user != null) {
            loginSuccess(user);
        } else {
            loginView.showError("Invalid username or password.");
        }
    }

    private void handleGoogleLogin() {
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
                            loginView.showError(
                                    "Google Sign In used a non-institutional email. Please use a @aau.edu.et email.");
                            return;
                        }

                        String username = email.split("@")[0];

                        User user = userDAO.getUserByUsername(username);
                        if (user == null) {

                            user = new User();
                            user.setUsername(username);
                            user.setEmail(email);
                            user.setPassword("GOOGLE_AUTH_PLACEHOLDER");

                            user.setUserType(UserType.STUDENT);
                            userDAO.createUser(user);

                            user = userDAO.getUserByUsername(username);
                        }

                        loginSuccess(user);

                    } else {
                        loginView.showError("Google Sign In failed or was cancelled.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    loginView.showError("An error occurred during Google Sign In.");
                }
            }
        }.execute();
    }

    private void loginSuccess(User user) {
        loginView.setVisible(false);
        loginView.clearFields();

        if (user.getUserType() == UserType.MANAGER) {
            ManagerDashboard managerDashboard = new ManagerDashboard(user.getUsername());
            new ManagerController(managerDashboard, user, this);
            managerDashboard.setVisible(true);
        } else {
            StudentDashboard studentDashboard = new StudentDashboard(user.getUsername());
            new StudentController(studentDashboard, user, this);
            studentDashboard.setVisible(true);
        }
    }

    private void handleSignUp() {
        SignUpView signUpView = new SignUpView(loginView);
        new SignUpController(signUpView, userDAO, loginView);
        signUpView.setVisible(true);
    }

    public void showLoginView() {
        loginView.setVisible(true);
    }
}
