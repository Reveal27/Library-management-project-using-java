package com.library.controller;

import com.library.dao.BookDAO;
import com.library.dao.BorrowRequestDAO;
import com.library.dao.IBorrowRequestDAO;
import com.library.dao.IStaffDAO;
import com.library.dao.StaffDAO;
import com.library.model.Book;
import com.library.model.BorrowRequest;
import com.library.model.Department;
import com.library.model.RequestStatus;
import com.library.model.Staff;
import com.library.model.User;
import com.library.view.ManagerDashboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class ManagerController {

    private ManagerDashboard managerDashboard;
    private BookDAO bookDAO;
    private IBorrowRequestDAO borrowRequestDAO;
    private IStaffDAO staffDAO;
    private LoginController loginController;

    public ManagerController(ManagerDashboard managerDashboard, User user, LoginController loginController) {
        this.managerDashboard = managerDashboard;
        this.bookDAO = new BookDAO();
        this.borrowRequestDAO = new BorrowRequestDAO();
        this.staffDAO = new StaffDAO();
        this.loginController = loginController;
        setupListeners();
        loadData();
    }

    private void setupListeners() {

        managerDashboard.setRefreshRequestsButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPendingRequests();
            }
        });

        managerDashboard.setApproveButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleApproveRequest();
            }
        });

        managerDashboard.setRejectButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRejectRequest();
            }
        });

        managerDashboard.setReturnButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleReturnBook();
            }
        });

        managerDashboard.setRefreshBooksButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBooks();
            }
        });

        managerDashboard.setAddBookButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddBook();
            }
        });

        managerDashboard.setRemoveBookButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRemoveBook();
            }
        });

        managerDashboard.setRefreshStaffButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadStaff();
            }
        });

        managerDashboard.setAddStaffButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddStaff();
            }
        });

        managerDashboard.setRemoveStaffButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRemoveStaff();
            }
        });

        managerDashboard.setLogoutButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogout();
            }
        });

        managerDashboard.setNotifyButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleNotifyStudent();
            }
        });
    }

    private void loadData() {
        loadPendingRequests();
        loadBooks();
        loadStaff();
    }

    private void loadPendingRequests() {
        borrowRequestDAO.checkOverdueRequests(); // Auto-update statuses
        var requests = borrowRequestDAO.getActiveRequests();
        managerDashboard.setRequests(requests);
    }

    private void loadBooks() {
        var books = bookDAO.getAllBooks();
        managerDashboard.setBooks(books);
    }

    private void loadStaff() {
        var staffList = staffDAO.getAllStaff();
        managerDashboard.setStaff(staffList);
    }

    private void handleApproveRequest() {
        int requestId = managerDashboard.getSelectedRequestId();
        if (requestId < 0) {
            managerDashboard.showError("Please select a request to approve.");
            return;
        }

        BorrowRequest request = borrowRequestDAO.getBorrowRequestById(requestId);
        if (request == null || request.getStatus() != RequestStatus.PENDING) {
            managerDashboard.showError("Invalid request selected.");
            return;
        }

        if (borrowRequestDAO.approveRequest(requestId)) {
            bookDAO.updateBookAvailability(request.getBookId(), false);

            managerDashboard.showSuccess(
                    "Request approved successfully!");
            loadPendingRequests();
            loadBooks();
        } else {
            managerDashboard.showError("Failed to approve request. Please try again.");
        }
    }

    private void handleRejectRequest() {
        int requestId = managerDashboard.getSelectedRequestId();
        if (requestId < 0) {
            managerDashboard.showError("Please select a request to reject.");
            return;
        }
        BorrowRequest request = borrowRequestDAO.getBorrowRequestById(requestId);
        if (request == null || request.getStatus() != RequestStatus.PENDING) {
            managerDashboard.showError("Invalid request selected.");
            return;
        }

        if (borrowRequestDAO.updateRequestStatus(requestId, RequestStatus.REJECTED)) {
            managerDashboard.showSuccess("Request rejected successfully.");
            loadPendingRequests();
        } else {
            managerDashboard.showError("Failed to reject request. Please try again.");
        }
    }

    private void handleReturnBook() {
        int requestId = managerDashboard.getSelectedRequestId();
        if (requestId < 0) {
            managerDashboard.showError("Please select a request to return.");
            return;
        }

        BorrowRequest request = borrowRequestDAO.getBorrowRequestById(requestId);
        if (request == null) {
            managerDashboard.showError("Request not found.");
            return;
        }

        if (request.getStatus() != RequestStatus.APPROVED && request.getStatus() != RequestStatus.OVERDUE) {
            managerDashboard
                    .showError("Only APPROVED or OVERDUE requests can be returned. Current status: "
                            + request.getStatus());
            return;
        }

        if (borrowRequestDAO.returnBook(requestId)) {
            bookDAO.updateBookAvailability(request.getBookId(), true);
            managerDashboard.showSuccess("Book returned successfully!");
            loadPendingRequests();
            loadBooks();
        } else {
            managerDashboard.showError("Failed to return book. Please try again.");
        }
    }

    private void handleNotifyStudent() {
        int requestId = managerDashboard.getSelectedRequestId();
        if (requestId < 0) {
            managerDashboard.showError("Please select a request to notify.");
            return;
        }

        BorrowRequest request = borrowRequestDAO.getBorrowRequestById(requestId);
        if (request == null) {
            managerDashboard.showError("Request not found.");
            return;
        }

        if (request.getStatus() != RequestStatus.OVERDUE) {
            managerDashboard.showError("You can only notify students for OVERDUE books.");
            return;
        }

        String email = request.getStudentEmail();
        if (email == null || email.isEmpty()) {
            managerDashboard.showError("No email address found for this student.");
            return;
        }

        try {
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            if (desktop.isSupported(java.awt.Desktop.Action.MAIL)) {
                String subject = "Overdue Book: " + request.getBookTitle();
                String body = "Dear Student,\n\nPlease return the book '" + request.getBookTitle()
                        + "' as soon as possible. It is now overdue.\n\nRegards,\nLibrary Manager";
                // Encode URI components simple way (Java 11 has URLEncoder)
                String uriStr = "mailto:" + email + "?subject="
                        + java.net.URLEncoder.encode(subject, "UTF-8").replace("+", "%20") + "&body="
                        + java.net.URLEncoder.encode(body, "UTF-8").replace("+", "%20");
                desktop.mail(new java.net.URI(uriStr));
            } else {
                managerDashboard.showError("Desktop Mail action is not supported on this system.");
            }
        } catch (Exception e) {
            managerDashboard.showError("Error launching mail client: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleAddBook() {
        String title = managerDashboard.getBookTitle();
        String author = managerDashboard.getBookAuthor();
        String isbn = managerDashboard.getBookIsbn();
        String quantityStr = managerDashboard.getBookQuantity();
        Department department = managerDashboard.getBookDepartment();

        if (title.isEmpty()) {
            managerDashboard.showError("Please enter a book title.");
            return;
        }

        int quantity = 1;
        try {
            if (!quantityStr.isEmpty()) {
                quantity = Integer.parseInt(quantityStr);
                if (quantity < 0) {
                    managerDashboard.showError("Quantity cannot be negative.");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            managerDashboard.showError("Invalid quantity format.");
            return;
        }

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author.isEmpty() ? null : author);
        book.setIsbn(isbn.isEmpty() ? null : isbn);
        book.setDepartment(department);
        book.setQuantity(quantity);
        book.setAvailable(quantity > 0);

        if (bookDAO.addBook(book)) {
            managerDashboard.showSuccess("Book added successfully!");
            managerDashboard.clearBookFields();
            loadBooks();
        } else {
            managerDashboard.showError("Failed to add book. Please try again.");
        }
    }

    private void handleRemoveBook() {
        int bookId = managerDashboard.getSelectedBookId();
        if (bookId < 0) {
            managerDashboard.showError("Please select a book to remove.");
            return;
        }

        java.util.List<BorrowRequest> requests = borrowRequestDAO.getRequestsByBookId(bookId);
        boolean hasActiveRequests = false;
        for (BorrowRequest r : requests) {
            if (r.getStatus() == RequestStatus.PENDING || r.getStatus() == RequestStatus.APPROVED
                    || r.getStatus() == RequestStatus.OVERDUE) {
                hasActiveRequests = true;
                break;
            }
        }

        if (hasActiveRequests) {
            managerDashboard.showError(
                    "Cannot remove book. There are active borrow requests (PENDING, APPROVED, or OVERDUE) for this book.");
            return;
        }

        if (hasActiveRequests) {
            managerDashboard.showError(
                    "Cannot remove book. There are active borrow requests (PENDING, APPROVED, or OVERDUE) for this book.");
            return;
        }

        if (!requests.isEmpty()) {
            // There are inactive requests (RETURNED, REJECTED)
            int confirmHistory = JOptionPane.showConfirmDialog(
                    managerDashboard,
                    "This book has " + requests.size() + " historical borrow records.\n" +
                            "Removing the book will PERMANENTLY DELETE these records.\n" +
                            "Are you sure you want to proceed?",
                    "Confirm History Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmHistory != JOptionPane.YES_OPTION) {
                return;
            }

            // Delete history first
            if (!borrowRequestDAO.deleteRequestsByBookId(bookId)) {
                managerDashboard.showError("Failed to delete historical records. Book removal cancelled.");
                return;
            }
        } else {
            int confirm = JOptionPane.showConfirmDialog(
                    managerDashboard,
                    "Are you sure you want to remove this book?",
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }

        if (bookDAO.removeBook(bookId)) {
            managerDashboard.showSuccess("Book removed successfully!");
            loadBooks();
        } else {
            managerDashboard.showError("Failed to remove book. Please try again.");
        }
    }

    private void handleAddStaff() {
        String name = managerDashboard.getStaffName();
        String position = managerDashboard.getStaffPosition();
        String email = managerDashboard.getStaffEmail();
        String phone = managerDashboard.getStaffPhone();

        if (name.isEmpty() || position.isEmpty()) {
            managerDashboard.showError("Please enter staff name and position.");
            return;
        }

        Staff staff = new Staff();
        staff.setName(name);
        staff.setPosition(position);
        staff.setEmail(email.isEmpty() ? null : email);
        staff.setPhone(phone.isEmpty() ? null : phone);

        if (staffDAO.addStaff(staff)) {
            managerDashboard.showSuccess("Staff member added successfully!");
            managerDashboard.clearStaffFields();
            loadStaff();
        } else {
            managerDashboard.showError("Failed to add staff member. Please try again.");
        }
    }

    private void handleRemoveStaff() {
        int staffId = managerDashboard.getSelectedStaffId();
        if (staffId < 0) {
            managerDashboard.showError("Please select a staff member to remove.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                managerDashboard,
                "Are you sure you want to remove this staff member?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (staffDAO.removeStaff(staffId)) {
                managerDashboard.showSuccess("Staff member removed successfully!");
                loadStaff();
            } else {
                managerDashboard.showError("Failed to remove staff member. Please try again.");
            }
        }
    }

    private void handleLogout() {
        managerDashboard.dispose();
        loginController.showLoginView();
    }
}
