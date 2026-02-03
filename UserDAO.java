package com.library.dao;

import com.library.database.DatabaseManager;
import com.library.model.Department;
import com.library.model.User;
import com.library.model.UserType;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {
    private DatabaseManager dbManager;

    public UserDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public User authenticate(String username, String password) {

        String sql = "SELECT id, username, password, email, user_type, department FROM users WHERE username = ?";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                boolean passwordMatch = false;
                try {
                    passwordMatch = BCrypt.checkpw(password, storedPassword);
                } catch (IllegalArgumentException e) {
                    if (storedPassword.equals(password)) {
                        passwordMatch = true;
                    }
                }

                if (passwordMatch) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(storedPassword);
                    user.setEmail(rs.getString("email"));
                    user.setUserType(UserType.valueOf(rs.getString("user_type")));
                    String deptStr = rs.getString("department");
                    if (deptStr != null) {
                        user.setDepartment(Department.valueOf(deptStr));
                    }
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean createUser(User user) {
        String sql = "INSERT INTO users (username, password, email, user_type, department) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, user.getEmail());

            pstmt.setString(4, user.getUserType().name());
            if (user.getDepartment() != null) {
                pstmt.setString(5, user.getDepartment().name());
            } else {
                pstmt.setString(5, null);
            }

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT id, username, password, email, user_type, department FROM users WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setUserType(UserType.valueOf(rs.getString("user_type")));
                String deptStr = rs.getString("department");
                if (deptStr != null) {
                    user.setDepartment(Department.valueOf(deptStr));
                }
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT id, username, password, email, user_type, department FROM users WHERE username = ?";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setUserType(UserType.valueOf(rs.getString("user_type")));
                String deptStr = rs.getString("department");
                if (deptStr != null) {
                    user.setDepartment(Department.valueOf(deptStr));
                }
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by username: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAllStudents() {
        List<User> students = new ArrayList<>();
        String sql = "SELECT id, username, password, email, user_type, department FROM users WHERE user_type = 'STUDENT'";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setUserType(UserType.STUDENT);
                String deptStr = rs.getString("department");
                if (deptStr != null) {
                    user.setDepartment(Department.valueOf(deptStr));
                }
                students.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all students: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, email = ?, user_type = ?, department = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            String password = user.getPassword();
            if (!password.startsWith("$2a$")) {
                password = BCrypt.hashpw(password, BCrypt.gensalt());
            }
            pstmt.setString(2, password);
            pstmt.setString(3, user.getEmail());

            pstmt.setString(4, user.getUserType().name());
            if (user.getDepartment() != null) {
                pstmt.setString(5, user.getDepartment().name());
            } else {
                pstmt.setString(5, null);
            }
            pstmt.setInt(6, user.getId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
