package com.library.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.library.model.Book;
import com.library.model.BorrowRequest;
import com.library.model.Department;
import com.library.model.Staff;

public class ManagerDashboard extends JFrame {
    private JTabbedPane tabbedPane;

    private JTable requestsTable;
    private DefaultTableModel requestsTableModel;
    private JButton approveButton;
    private JButton rejectButton;
    private JButton returnButton;
    private JButton notifyButton;

    private JButton refreshRequestsButton;

    private JTable booksTable;
    private DefaultTableModel booksTableModel;
    private JTextField bookTitleField;
    private JTextField bookAuthorField;
    private JTextField bookIsbnField;
    private JTextField bookQuantityField;
    private JComboBox<Department> bookDeptCombo;
    private JButton addBookButton;
    private JButton removeBookButton;
    private JButton refreshBooksButton;
    private JTable staffTable;
    private DefaultTableModel staffTableModel;
    private JTextField staffNameField;
    private JTextField staffPositionField;
    private JTextField staffEmailField;
    private JTextField staffPhoneField;
    private JButton addStaffButton;
    private JButton removeStaffButton;
    private JButton refreshStaffButton;

    private JButton logoutButton;
    private JLabel welcomeLabel;
    private String currentUsername;

    public ManagerDashboard(String username) {
        this.currentUsername = username;
        initializeComponents();
        setupLayout();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Library Management System - Manager Dashboard");
        setSize(1200, 800);
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        welcomeLabel = new JLabel("Welcome, Manager " + currentUsername + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setForeground(new Color(70, 130, 180));
        logoutButton.setFocusPainted(false);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.addTab("Borrow Requests", createRequestsPanel());

        tabbedPane.addTab("Book Inventory", createBooksPanel());

        tabbedPane.addTab("Staff Management", createStaffPanel());

        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createRequestsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columnNames = { "ID", "Student", "Book Title", "Request Date", "Return Date", "Status" };
        requestsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        requestsTable = new JTable(requestsTableModel);
        requestsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        requestsTable.setRowHeight(25);
        requestsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        requestsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane tableScrollPane = new JScrollPane(requestsTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Pending Borrow Requests"));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        refreshRequestsButton = new JButton("Refresh");
        refreshRequestsButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        refreshRequestsButton.setFocusPainted(false);
        buttonPanel.add(refreshRequestsButton);

        approveButton = new JButton("Approve Request");
        approveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        approveButton.setPreferredSize(new Dimension(180, 35));
        approveButton.setBackground(new Color(70, 130, 180));
        approveButton.setForeground(Color.WHITE);
        approveButton.setFocusPainted(false);
        rejectButton = new JButton("Reject Request");
        rejectButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        rejectButton.setPreferredSize(new Dimension(180, 35));
        rejectButton.setBackground(new Color(220, 50, 90));
        rejectButton.setForeground(Color.WHITE);
        rejectButton.setFocusPainted(false);

        returnButton = new JButton("Return Book");
        returnButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        returnButton.setPreferredSize(new Dimension(180, 35));
        returnButton.setBackground(new Color(40, 167, 69)); // Success Green
        returnButton.setForeground(Color.WHITE);
        returnButton.setFocusPainted(false);

        notifyButton = new JButton("Notify Student");
        notifyButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        notifyButton.setPreferredSize(new Dimension(180, 35));
        notifyButton.setBackground(new Color(255, 193, 7)); // Warning Yellow
        notifyButton.setForeground(Color.BLACK);
        notifyButton.setFocusPainted(false);

        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(notifyButton); // Add to panel with others
        buttonPanel.add(returnButton);

        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columnNames = { "ID", "Title", "Author", "ISBN", "Department", "Quantity", "Available" };
        booksTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        booksTable = new JTable(booksTableModel);
        booksTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        booksTable.setRowHeight(25);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        booksTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane tableScrollPane = new JScrollPane(booksTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Book Inventory"));
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Book"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        bookTitleField = new JTextField(20);
        bookTitleField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(bookTitleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        bookAuthorField = new JTextField(20);
        bookAuthorField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(bookAuthorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        bookIsbnField = new JTextField(20);
        bookIsbnField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(bookIsbnField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        bookQuantityField = new JTextField(20);
        bookQuantityField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(bookQuantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        bookDeptCombo = new JComboBox<>(Department.values());
        bookDeptCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(bookDeptCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel bookButtonPanel = new JPanel(new FlowLayout());
        addBookButton = new JButton("Add Book");
        addBookButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addBookButton.setPreferredSize(new Dimension(120, 35));
        addBookButton.setBackground(new Color(70, 130, 180));
        addBookButton.setForeground(Color.WHITE);
        addBookButton.setFocusPainted(false);
        bookButtonPanel.add(addBookButton);

        removeBookButton = new JButton("Remove Selected");
        removeBookButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        removeBookButton.setPreferredSize(new Dimension(140, 35));
        removeBookButton.setBackground(new Color(220, 53, 69));
        removeBookButton.setForeground(Color.WHITE);
        removeBookButton.setFocusPainted(false);
        bookButtonPanel.add(removeBookButton);

        refreshBooksButton = new JButton("Refresh");
        refreshBooksButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        refreshBooksButton.setFocusPainted(false);
        bookButtonPanel.add(refreshBooksButton);

        formPanel.add(bookButtonPanel, gbc);

        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStaffPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        String[] columnNames = { "ID", "Name", "Position", "Email", "Phone" };
        staffTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        staffTable = new JTable(staffTableModel);
        staffTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        staffTable.setRowHeight(25);
        staffTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        staffTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane tableScrollPane = new JScrollPane(staffTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Staff Members"));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Staff Member"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        staffNameField = new JTextField(20);
        staffNameField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(staffNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Position:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        staffPositionField = new JTextField(20);
        staffPositionField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(staffPositionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        staffEmailField = new JTextField(20);
        staffEmailField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(staffEmailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        staffPhoneField = new JTextField(20);
        staffPhoneField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(staffPhoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel staffButtonPanel = new JPanel(new FlowLayout());
        addStaffButton = new JButton("Add Staff");
        addStaffButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addStaffButton.setPreferredSize(new Dimension(120, 35));
        addStaffButton.setBackground(new Color(70, 130, 180));
        addStaffButton.setForeground(Color.WHITE);
        addStaffButton.setFocusPainted(false);
        staffButtonPanel.add(addStaffButton);

        removeStaffButton = new JButton("Remove Selected");
        removeStaffButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        removeStaffButton.setPreferredSize(new Dimension(140, 35));
        removeStaffButton.setBackground(new Color(220, 53, 69));
        removeStaffButton.setForeground(Color.WHITE);
        removeStaffButton.setFocusPainted(false);
        staffButtonPanel.add(removeStaffButton);

        refreshStaffButton = new JButton("Refresh");
        refreshStaffButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        refreshStaffButton.setFocusPainted(false);
        staffButtonPanel.add(refreshStaffButton);

        formPanel.add(staffButtonPanel, gbc);

        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void setupLayout() {
    }

    public void setRequests(List<BorrowRequest> requests) {
        requestsTableModel.setRowCount(0);
        for (BorrowRequest request : requests) {
            Object[] row = {
                    request.getId(),
                    request.getStudentUsername() != null ? request.getStudentUsername() : "N/A",
                    request.getBookTitle() != null ? request.getBookTitle() : "N/A",
                    request.getRequestDate() != null ? request.getRequestDate() : "N/A",
                    request.getReturnDate(),
                    request.getStatus().name()
            };
            requestsTableModel.addRow(row);
        }
    }

    public int getSelectedRequestId() {
        int selectedRow = requestsTable.getSelectedRow();
        if (selectedRow >= 0) {
            return (Integer) requestsTableModel.getValueAt(selectedRow, 0);
        }
        return -1;
    }

    public void setBooks(List<Book> books) {
        booksTableModel.setRowCount(0);
        for (Book book : books) {
            Object[] row = {
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor() != null ? book.getAuthor() : "N/A",
                    book.getIsbn() != null ? book.getIsbn() : "N/A",
                    book.getDepartment().getDisplayName(),
                    book.getQuantity(),
                    book.isAvailable() ? "Yes" : "No"
            };
            booksTableModel.addRow(row);
        }
    }

    public int getSelectedBookId() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow >= 0) {
            return (Integer) booksTableModel.getValueAt(selectedRow, 0);
        }
        return -1;
    }

    public String getBookTitle() {
        return bookTitleField.getText().trim();
    }

    public String getBookAuthor() {
        return bookAuthorField.getText().trim();
    }

    public String getBookIsbn() {
        return bookIsbnField.getText().trim();
    }

    public String getBookQuantity() {
        return bookQuantityField.getText().trim();
    }

    public Department getBookDepartment() {
        return (Department) bookDeptCombo.getSelectedItem();
    }

    public void clearBookFields() {
        bookTitleField.setText("");
        bookAuthorField.setText("");
        bookIsbnField.setText("");
        bookQuantityField.setText("");
        bookDeptCombo.setSelectedIndex(0);
    }

    public void setStaff(List<Staff> staffList) {
        staffTableModel.setRowCount(0);
        for (Staff staff : staffList) {
            Object[] row = {
                    staff.getId(),
                    staff.getName(),
                    staff.getPosition(),
                    staff.getEmail() != null ? staff.getEmail() : "N/A",
                    staff.getPhone() != null ? staff.getPhone() : "N/A"
            };
            staffTableModel.addRow(row);
        }
    }

    public int getSelectedStaffId() {
        int selectedRow = staffTable.getSelectedRow();
        if (selectedRow >= 0) {
            return (Integer) staffTableModel.getValueAt(selectedRow, 0);
        }
        return -1;
    }

    public String getStaffName() {
        return staffNameField.getText().trim();
    }

    public String getStaffPosition() {
        return staffPositionField.getText().trim();
    }

    public String getStaffEmail() {
        return staffEmailField.getText().trim();
    }

    public String getStaffPhone() {
        return staffPhoneField.getText().trim();
    }

    public void clearStaffFields() {
        staffNameField.setText("");
        staffPositionField.setText("");
        staffEmailField.setText("");
        staffPhoneField.setText("");
    }

    public void setApproveButtonListener(ActionListener listener) {
        approveButton.addActionListener(listener);
    }

    public void setRejectButtonListener(ActionListener listener) {
        rejectButton.addActionListener(listener);
    }

    public void setReturnButtonListener(ActionListener listener) {
        returnButton.addActionListener(listener);
    }

    public void setNotifyButtonListener(ActionListener listener) {
        notifyButton.addActionListener(listener);
    }

    public void setRefreshRequestsButtonListener(ActionListener listener) {
        refreshRequestsButton.addActionListener(listener);
    }

    public void setAddBookButtonListener(ActionListener listener) {
        addBookButton.addActionListener(listener);
    }

    public void setRemoveBookButtonListener(ActionListener listener) {
        removeBookButton.addActionListener(listener);
    }

    public void setRefreshBooksButtonListener(ActionListener listener) {
        refreshBooksButton.addActionListener(listener);
    }

    public void setAddStaffButtonListener(ActionListener listener) {
        addStaffButton.addActionListener(listener);
    }

    public void setRemoveStaffButtonListener(ActionListener listener) {
        removeStaffButton.addActionListener(listener);
    }

    public void setRefreshStaffButtonListener(ActionListener listener) {
        refreshStaffButton.addActionListener(listener);
    }

    public void setLogoutButtonListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
