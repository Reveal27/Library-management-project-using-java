package com.library.dao;

import com.library.database.DatabaseManager;
import com.library.model.BorrowRequest;
import com.library.model.RequestStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BorrowRequestDAO implements IBorrowRequestDAO {
    private DatabaseManager dbManager;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public BorrowRequestDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public boolean createBorrowRequest(BorrowRequest request) {
        String sql = "INSERT INTO borrow_requests (student_id, book_id, request_date, return_date, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, request.getStudentId());
            pstmt.setInt(2, request.getBookId());
            pstmt.setString(3, dateFormat.format(new Date()));
            pstmt.setString(4, request.getReturnDate());
            pstmt.setString(5, RequestStatus.PENDING.name());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error creating borrow request: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public BorrowRequest getBorrowRequestById(int id) {
        String sql = "SELECT br.id, br.student_id, br.book_id, br.request_date, br.return_date, " +
                "br.status, br.approved_at, br.returned_at, " +
                "u.username as student_username, u.email as student_email, b.title as book_title " +
                "FROM borrow_requests br " +
                "LEFT JOIN users u ON br.student_id = u.id " +
                "LEFT JOIN books b ON br.book_id = b.id " +
                "WHERE br.id = ?";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToBorrowRequest(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting borrow request by id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BorrowRequest> getActiveRequests() {
        List<BorrowRequest> requests = new ArrayList<>();
        String sql = "SELECT br.id, br.student_id, br.book_id, br.request_date, br.return_date, " +
                "br.status, br.approved_at, br.returned_at, " +
                "u.username as student_username, u.email as student_email, b.title as book_title " +
                "FROM borrow_requests br " +
                "LEFT JOIN users u ON br.student_id = u.id " +
                "LEFT JOIN books b ON br.book_id = b.id " +
                "WHERE br.status IN ('PENDING', 'APPROVED', 'OVERDUE') " +
                "ORDER BY br.request_date DESC";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                requests.add(mapResultSetToBorrowRequest(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting active requests: " + e.getMessage());
            e.printStackTrace();
        }
        return requests;
    }

    @Override
    public List<BorrowRequest> getRequestsByStudentId(int studentId) {
        List<BorrowRequest> requests = new ArrayList<>();
        String sql = "SELECT br.id, br.student_id, br.book_id, br.request_date, br.return_date, " +
                "br.status, br.approved_at, br.returned_at, " +
                "u.username as student_username, u.email as student_email, b.title as book_title " +
                "FROM borrow_requests br " +
                "LEFT JOIN users u ON br.student_id = u.id " +
                "LEFT JOIN books b ON br.book_id = b.id " +
                "WHERE br.student_id = ? " +
                "ORDER BY br.request_date DESC";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                requests.add(mapResultSetToBorrowRequest(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting requests by student id: " + e.getMessage());
            e.printStackTrace();
        }
        return requests;
    }

    @Override
    public boolean updateRequestStatus(int requestId, RequestStatus status) {
        String sql = "UPDATE borrow_requests SET status = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status.name());
            pstmt.setInt(2, requestId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating request status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean approveRequest(int requestId) {
        String sql = "UPDATE borrow_requests SET status = ?, approved_at = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, RequestStatus.APPROVED.name());
            pstmt.setString(2, dateFormat.format(new Date()));
            pstmt.setInt(3, requestId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error approving request: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean returnBook(int requestId) {
        String sql = "UPDATE borrow_requests SET status = ?, returned_at = ? WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, RequestStatus.RETURNED.name());
            pstmt.setString(2, dateFormat.format(new Date()));
            pstmt.setInt(3, requestId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error returning book: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<BorrowRequest> getRequestsByBookId(int bookId) {
        List<BorrowRequest> requests = new ArrayList<>();
        String sql = "SELECT br.id, br.student_id, br.book_id, br.request_date, br.return_date, " +
                "br.status, br.approved_at, br.returned_at, " +
                "u.username as student_username, u.email as student_email, b.title as book_title " +
                "FROM borrow_requests br " +
                "LEFT JOIN users u ON br.student_id = u.id " +
                "LEFT JOIN books b ON br.book_id = b.id " +
                "WHERE br.book_id = ? " +
                "ORDER BY br.request_date DESC";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                requests.add(mapResultSetToBorrowRequest(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting requests by book id: " + e.getMessage());
            e.printStackTrace();
        }
        return requests;
    }

    @Override
    public boolean deleteRequestsByBookId(int bookId) {
        String sql = "DELETE FROM borrow_requests WHERE book_id = ?";
        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting requests by book id: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void checkOverdueRequests() {
        String sql = "UPDATE borrow_requests SET status = ? WHERE status = ? AND return_date < CURDATE()";

        try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, RequestStatus.OVERDUE.name());
            pstmt.setString(2, RequestStatus.APPROVED.name());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Marked " + rowsAffected + " requests as OVERDUE.");
            }
        } catch (SQLException e) {
            System.err.println("Error checking overdue requests: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private BorrowRequest mapResultSetToBorrowRequest(ResultSet rs) throws SQLException {
        BorrowRequest request = new BorrowRequest();
        request.setId(rs.getInt("id"));
        request.setStudentId(rs.getInt("student_id"));
        request.setBookId(rs.getInt("book_id"));
        request.setRequestDate(rs.getString("request_date"));
        request.setReturnDate(rs.getString("return_date"));
        request.setStatus(RequestStatus.valueOf(rs.getString("status")));
        request.setApprovedAt(rs.getString("approved_at"));
        request.setReturnedAt(rs.getString("returned_at"));
        request.setStudentUsername(rs.getString("student_username"));
        request.setBookTitle(rs.getString("book_title"));
        try {
            request.setStudentEmail(rs.getString("student_email"));
        } catch (SQLException e) {
        }
        return request;
    }
}
