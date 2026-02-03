package com.library.model;

public enum Department {
    BIOMEDICAL("Biomedical Engineering"),
    CHEMICAL("Chemical Engineering"),
    CIVIL("Civil Engineering"),
    ELECTRICAL_COMPUTER("Electrical & Computer Engineering"),
    MECHANICAL("Mechanical Engineering"),
    SOFTWARE("Software Engineering");
    
    private final String displayName;
    
    Department(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
    @Override
    public String toString() {
        return displayName;
    }
}






