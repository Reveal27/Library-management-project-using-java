
package com.library.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static DatabaseManager instance;

    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "my_library";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "1234";

    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME +
            "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    private Connection connection;

    private DatabaseManager() {
        initializeDatabase();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initializeDatabase() {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            createTablesFromScript();

            System.out.println("MySQL Database initialized successfully: " + DB_NAME);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createTablesFromScript() {
        try {
            String sqlScript = new String(Files.readAllBytes(Paths.get("db_setup.sql")));
            String[] lines = sqlScript.split("\n");
            StringBuilder currentStatement = new StringBuilder();

            try (Statement stmt = connection.createStatement()) {
                for (String line : lines) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty() || trimmed.startsWith("--")) continue;

                    int commentIndex = trimmed.indexOf("--");
                    if (commentIndex >= 0) trimmed = trimmed.substring(0, commentIndex).trim();
                    if (trimmed.isEmpty()) continue;

                    currentStatement.append(trimmed).append(" ");

                    if (trimmed.endsWith(";")) {
                        String statement = currentStatement.toString().trim();
                        try {
                            stmt.execute(statement);
                        } catch (SQLException e) {
                            System.out.println("Note: " + e.getMessage());
                        }
                        currentStatement.setLength(0);
                    }
                }
                ensureManagerAccount(stmt);
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not read db_setup.sql. Using hardcoded schema.");
            createTablesHardcoded();
        } catch (SQLException e) {
            System.err.println("Error executing SQL script: " + e.getMessage());
            createTablesHardcoded();
        }
    }

    private void ensureManagerAccount(Statement stmt) {
        try {
            stmt.execute("INSERT IGNORE INTO users (username, password, user_type) " +
                    "VALUES ('manager', 'manager123', 'MANAGER')");
            System.out.println("Manager account verified/created.");
        } catch (SQLException e) {
            System.err.println("Warning: Could not ensure manager account exists: " + e.getMessage());
        }
    }

    private void createTablesHardcoded() {
        try (Statement stmt = connection.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "username VARCHAR(255) UNIQUE NOT NULL," +
                    "password VARCHAR(255) NOT NULL," +
                    "user_type VARCHAR(50) NOT NULL," +
                    "department VARCHAR(255)," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");
            stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "title VARCHAR(255) NOT NULL," +
                    "author VARCHAR(255)," +
                    "isbn VARCHAR(50)," +
                    "department VARCHAR(100) NOT NULL," +
                    "is_available TINYINT(1) DEFAULT 1," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");
            stmt.execute("CREATE TABLE IF NOT EXISTS borrow_requests (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "student_id INT NOT NULL," +
                    "book_id INT NOT NULL," +
                    "request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "return_date DATE NOT NULL," +
                    "status VARCHAR(20) DEFAULT 'PENDING'," +
                    "approved_at DATETIME," +
                    "returned_at DATETIME," +
                    "FOREIGN KEY (student_id) REFERENCES users(id)," +
                    "FOREIGN KEY (book_id) REFERENCES books(id)" +
                    ")");

            stmt.execute("CREATE TABLE IF NOT EXISTS staff (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(255) NOT NULL," +
                    "position VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255)," +
                    "phone VARCHAR(20)," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");

            ensureManagerAccount(stmt);
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
