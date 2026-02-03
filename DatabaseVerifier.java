package com.library.util;

import com.library.database.DatabaseManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseVerifier {
    
    public static void verifyAndDisplayDatabase() {
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            Connection conn = dbManager.getConnection();
            
            System.out.println("\n=== Database Verification ===");
            

            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users");
                if (rs.next()) {
                    int userCount = rs.getInt("count");
                    System.out.println("Total users in database: " + userCount);
                    
                    if (userCount == 0) {
                        System.out.println("WARNING: No users found! Creating default manager account...");
                        createDefaultManager(stmt);
                    } else {
                        rs = stmt.executeQuery("SELECT id, username, user_type, department FROM users");
                        System.out.println("\nUsers in database:");
                        while (rs.next()) {
                            System.out.println("  ID: " + rs.getInt("id") + 
                                           ", Username: " + rs.getString("username") + 
                                           ", Type: " + rs.getString("user_type") +
                                           ", Department: " + (rs.getString("department") != null ? rs.getString("department") : "N/A"));
                        }
                    }
                }
                

                rs = stmt.executeQuery("SELECT COUNT(*) as count FROM books");
                if (rs.next()) {
                    System.out.println("\nTotal books in database: " + rs.getInt("count"));
                }

                rs = stmt.executeQuery("SELECT COUNT(*) as count FROM staff");
                if (rs.next()) {
                    System.out.println("\nTotal staff in database: " + rs.getInt("count"));
                }
                
            } catch (Exception e) {
                System.err.println("Error verifying database: " + e.getMessage());
                e.printStackTrace();
            }
            
            System.out.println("=== End Database Verification ===\n");
            
        } catch (Exception e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void createDefaultManager(Statement stmt) {
        try {
            stmt.execute("INSERT OR IGNORE INTO users (username, password, user_type) " +
                        "VALUES ('manager', 'manager123', 'MANAGER')");
            System.out.println("Default manager account created: username='manager', password='manager123'");
        } catch (Exception e) {
            System.err.println("Error creating default manager: " + e.getMessage());
            e.printStackTrace();
        }
    }
}



