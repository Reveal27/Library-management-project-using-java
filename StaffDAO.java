package com.library.dao;

import com.library.database.DatabaseManager;
import com.library.model.Staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class StaffDAO implements IStaffDAO {
    private DatabaseManager dbManager;
    
    public StaffDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    @Override
    public boolean addStaff(Staff staff) {
        String sql = "INSERT INTO staff (name, position, email, phone) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, staff.getName());
            pstmt.setString(2, staff.getPosition());
            pstmt.setString(3, staff.getEmail());
            pstmt.setString(4, staff.getPhone());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding staff: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean removeStaff(int staffId) {
        String sql = "DELETE FROM staff WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, staffId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error removing staff: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Staff getStaffById(int id) {
        String sql = "SELECT id, name, position, email, phone FROM staff WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Staff staff = new Staff();
                staff.setId(rs.getInt("id"));
                staff.setName(rs.getString("name"));
                staff.setPosition(rs.getString("position"));
                staff.setEmail(rs.getString("email"));
                staff.setPhone(rs.getString("phone"));
                return staff;
            }
        } catch (SQLException e) {
            System.err.println("Error getting staff by id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT id, name, position, email, phone FROM staff ORDER BY name";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Staff staff = new Staff();
                staff.setId(rs.getInt("id"));
                staff.setName(rs.getString("name"));
                staff.setPosition(rs.getString("position"));
                staff.setEmail(rs.getString("email"));
                staff.setPhone(rs.getString("phone"));
                staffList.add(staff);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all staff: " + e.getMessage());
            e.printStackTrace();
        }
        return staffList;
    }
    
    @Override
    public boolean updateStaff(Staff staff) {
        String sql = "UPDATE staff SET name = ?, position = ?, email = ?, phone = ? WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, staff.getName());
            pstmt.setString(2, staff.getPosition());
            pstmt.setString(3, staff.getEmail());
            pstmt.setString(4, staff.getPhone());
            pstmt.setInt(5, staff.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating staff: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}






