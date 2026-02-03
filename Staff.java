package com.library.model;

public class Staff {
    private int id;
    private String name;
    private String position;
    private String email;
    private String phone;
    
    public Staff() {
    }
    
    public Staff(int id, String name, String position, String email, String phone) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.email = email;
        this.phone = phone;
    }
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
}






