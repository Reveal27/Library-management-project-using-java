package com.library.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private UserType userType;
    private Department department;

    public User() {
    }

    public User(int id, String username, String password, String email, UserType userType, Department department) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.userType = userType;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
