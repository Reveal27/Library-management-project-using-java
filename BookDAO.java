package com.library.dao;

import com.library.database.DatabaseManager;
import com.library.model.Book;
import com.library.model.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO implements IBookDAO {
    private DatabaseManager dbManager;

    public BookDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public boolean addBook(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, department, is_available, quantity) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setString(4, book.getDepartment().name());
            pstmt.setInt(5, book.isAvailable() ? 1 : 0);
            pstmt.setInt(6, book.getQuantity());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeBook(int bookId) {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error removing book: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Book getBookById(int id) {
        String sql = "SELECT id, title, author, isbn, department, is_available, quantity FROM books WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setDepartment(Department.valueOf(rs.getString("department")));
                book.setAvailable(rs.getInt("is_available") == 1);
                book.setQuantity(rs.getInt("quantity"));
                return book;
            }
        } catch (SQLException e) {
            System.err.println("Error getting book by id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, title, author, isbn, department, is_available, quantity FROM books";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setDepartment(Department.valueOf(rs.getString("department")));
                book.setAvailable(rs.getInt("is_available") == 1);
                book.setQuantity(rs.getInt("quantity"));
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all books: " + e.getMessage());
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public List<Book> getBooksByDepartment(Department department) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, title, author, isbn, department, is_available, quantity FROM books WHERE department = ?";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, department.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setDepartment(Department.valueOf(rs.getString("department")));
                book.setAvailable(rs.getInt("is_available") == 1);
                book.setQuantity(rs.getInt("quantity"));
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Error getting books by department: " + e.getMessage());
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public List<Book> getAvailableBooksByDepartment(Department department) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, title, author, isbn, department, is_available, quantity FROM books WHERE department = ? AND quantity > 0";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, department.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setDepartment(Department.valueOf(rs.getString("department")));
                book.setQuantity(rs.getInt("quantity"));
                book.setAvailable(true); // Logic driven by quantity query
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Error getting available books by department: " + e.getMessage());
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public boolean updateBookAvailability(int bookId, boolean isAvailable) {
        String sql = "";
        if (isAvailable) {
            sql = "UPDATE books SET quantity = quantity + 1 WHERE id = ?";
        } else {
            sql = "UPDATE books SET quantity = CASE WHEN quantity > 0 THEN quantity - 1 ELSE 0 END WHERE id = ?";
        }

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            int rowsAffected = pstmt.executeUpdate();

            updateLegacyAvailability(bookId);

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating book quantity: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void updateLegacyAvailability(int bookId) {
        String sql = "UPDATE books SET is_available = (CASE WHEN quantity > 0 THEN 1 ELSE 0 END) WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, department = ?, is_available = ?, quantity = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setString(4, book.getDepartment().name());
            pstmt.setInt(5, book.getQuantity() > 0 ? 1 : 0);
            pstmt.setInt(6, book.getQuantity());
            pstmt.setInt(7, book.getId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
