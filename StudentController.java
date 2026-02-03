package com.library.controller;
import com.library.dao.BookDAO;
import com.library.dao.BorrowRequestDAO;
import com.library.dao.IBookDAO;
import com.library.dao.IBorrowRequestDAO;
import com.library.model.Book;
import com.library.model.BorrowRequest;
import com.library.model.Department;
import com.library.model.User;

import com.library.view.StudentDashboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class StudentController {
    private StudentDashboard studentDashboard;
    private User currentUser;
    private IBookDAO bookDAO;
    private IBorrowRequestDAO borrowRequestDAO;
    private LoginController loginController;

    public StudentController(StudentDashboard studentDashboard, User user, LoginController loginController) {
        this.studentDashboard = studentDashboard;
        this.currentUser = user;
        this.bookDAO = new BookDAO();
        this.borrowRequestDAO = new BorrowRequestDAO();
        this.loginController = loginController;
        setupListeners();
        loadBooks();
        loadMyRequests();
    }

    private void setupListeners() {
        studentDashboard.setDepartmentComboListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBooks();
            }
        });

        studentDashboard.setRefreshButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBooks();
            }
        });

        studentDashboard.setBorrowButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBorrowRequest();
            }
        });

        studentDashboard.setLogoutButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogout();
            }
        });

        studentDashboard.setRefreshRequestsButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMyRequests();
            }
        });
    }

    private void loadBooks() {
        Department selectedDept = studentDashboard.getSelectedDepartment();
        if (selectedDept != null) {
            var books = bookDAO.getBooksByDepartment(selectedDept);
            studentDashboard.setBooks(books);
        }
    }

    private void handleBorrowRequest() {
        String bookTitle = studentDashboard.getBookTitle();
        String returnDate = studentDashboard.getReturnDate();

        if (bookTitle.isEmpty() || returnDate.isEmpty()) {
            studentDashboard.showError("Please enter book title and return date.");
            return;
        }


        if (!returnDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            studentDashboard.showError("Invalid date format. Please use YYYY-MM-DD format.");
            return;
        }

        var books = bookDAO.getAllBooks();
        Book selectedBook = null;
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(bookTitle) && book.isAvailable()) {
                selectedBook = book;
                break;
            }
        }

        if (selectedBook == null) {
            studentDashboard.showError("Book not found or not available. Please check the title.");
            return;
        }

        BorrowRequest request = new BorrowRequest();
        request.setStudentId(currentUser.getId());
        request.setBookId(selectedBook.getId());
        request.setReturnDate(returnDate);

        if (borrowRequestDAO.createBorrowRequest(request)) {
            studentDashboard.showSuccess(
                    "Borrow request submitted successfully! It is now pending for manager approval. Check 'My Requests' tab to see status updates.");
            studentDashboard.clearBorrowFields();
            loadBooks();
            loadMyRequests();
        } else {
            studentDashboard.showError("Failed to submit borrow request. Please try again.");
        }
    }

    private void loadMyRequests() {
        var requests = borrowRequestDAO.getRequestsByStudentId(currentUser.getId());
        studentDashboard.setRequests(requests);
    }

    private void handleLogout() {
        studentDashboard.dispose();
        loginController.showLoginView();
    }
}
