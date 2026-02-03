package com.library.view;

import com.library.model.Book;
import com.library.model.BorrowRequest;
import com.library.model.Department;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StudentDashboard extends JFrame {
    private JTabbedPane tabbedPane;
    private JComboBox<Department> departmentCombo;
    private JTable booksTable;
    private DefaultTableModel booksTableModel;
    private JTextField titleField;
    private JTextField returnDateField;
    private JButton borrowButton;
    private JButton refreshButton;
    private JButton logoutButton;
    private JLabel welcomeLabel;
    

    private JTable requestsTable;
    private DefaultTableModel requestsTableModel;
    private JButton refreshRequestsButton;
    
    private String currentUsername;
    
    public StudentDashboard(String username) {
        this.currentUsername = username;
        initializeComponents();
        setupLayout();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Library Management System - Student Dashboard");
        setSize(1000, 700);
        setLocationRelativeTo(null);
    }
    
    private void initializeComponents() {
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        welcomeLabel = new JLabel("Welcome, " + currentUsername + "!");
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

        tabbedPane.addTab("Browse Books", createBrowseBooksPanel());
        

        tabbedPane.addTab("My Requests", createMyRequestsPanel());
        
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createBrowseBooksPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel deptPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        deptPanel.setBorder(BorderFactory.createTitledBorder("Select Department"));
        JLabel deptLabel = new JLabel("Department:");
        deptLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        deptPanel.add(deptLabel);
        
        departmentCombo = new JComboBox<>(Department.values());
        departmentCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        departmentCombo.setPreferredSize(new Dimension(300, 30));
        deptPanel.add(departmentCombo);
        
        refreshButton = new JButton("Refresh Books");
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        refreshButton.setFocusPainted(false);
        deptPanel.add(refreshButton);

        String[] columnNames = {"ID", "Title", "Author", "ISBN", "Department", "Available"};
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
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Available Books"));
        tableScrollPane.setPreferredSize(new Dimension(0, 300));

        JPanel borrowPanel = new JPanel(new GridBagLayout());
        borrowPanel.setBorder(BorderFactory.createTitledBorder("Borrow Book"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel titleLabel = new JLabel("Book Title:");
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        borrowPanel.add(titleLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        titleField = new JTextField(20);
        titleField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleField.setPreferredSize(new Dimension(300, 30));
        borrowPanel.add(titleField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel returnDateLabel = new JLabel("Return Date (YYYY-MM-DD):");
        returnDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        borrowPanel.add(returnDateLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        returnDateField = new JTextField(20);
        returnDateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        returnDateField.setPreferredSize(new Dimension(300, 30));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date returnDate = new Date(System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000));
        returnDateField.setText(sdf.format(returnDate));
        borrowPanel.add(returnDateField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        borrowButton = new JButton("Submit Borrow Request");
        borrowButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        borrowButton.setPreferredSize(new Dimension(200, 40));
        borrowButton.setBackground(new Color(70, 130, 180));
        borrowButton.setForeground(Color.WHITE);
        borrowButton.setFocusPainted(false);
        borrowPanel.add(borrowButton, gbc);
        

        mainPanel.add(deptPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(borrowPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JPanel createMyRequestsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        

        String[] columnNames = {"ID", "Book Title", "Request Date", "Return Date", "Status", "Approved At"};
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

        requestsTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (column == 4 && value != null) {
                    String status = value.toString();
                    if (status.equals("APPROVED")) {
                        c.setBackground(new Color(144, 238, 144)); // Light green
                        c.setForeground(Color.BLACK);
                    } else if (status.equals("PENDING")) {
                        c.setBackground(new Color(255, 255, 153)); // Light yellow
                        c.setForeground(Color.BLACK);
                    } else if (status.equals("REJECTED")) {
                        c.setBackground(new Color(255, 182, 193)); // Light red
                        c.setForeground(Color.BLACK);
                    } else if (status.equals("RETURNED")) {
                        c.setBackground(new Color(176, 224, 230)); // Light blue
                        c.setForeground(Color.BLACK);
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                } else {
                    if (!isSelected) {
                        c.setBackground(Color.WHITE);
                    }
                }
                
                return c;
            }
        });
        
        JScrollPane tableScrollPane = new JScrollPane(requestsTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("My Borrow Requests"));

        JLabel infoLabel = new JLabel("<html><b>Status Legend:</b> " +
                "<span style='background-color: #FFFF99; padding: 2px 5px;'>PENDING</span> - " +
                "<span style='background-color: #90EE90; padding: 2px 5px;'>APPROVED</span> - " +
                "<span style='background-color: #FFB6C1; padding: 2px 5px;'>REJECTED</span> - " +
                "<span style='background-color: #B0E0E6; padding: 2px 5px;'>RETURNED</span></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        refreshRequestsButton = new JButton("Refresh Requests");
        refreshRequestsButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        refreshRequestsButton.setFocusPainted(false);
        buttonPanel.add(refreshRequestsButton);
        
        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(infoLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void setupLayout() {
    }

    public Department getSelectedDepartment() {
        return (Department) departmentCombo.getSelectedItem();
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
                book.isAvailable() ? "Yes" : "No"
            };
            booksTableModel.addRow(row);
        }
    }
    
    public String getBookTitle() {
        return titleField.getText().trim();
    }
    
    public String getReturnDate() {
        return returnDateField.getText().trim();
    }
    
    public void clearBorrowFields() {
        titleField.setText("");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date returnDate = new Date(System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000));
        returnDateField.setText(sdf.format(returnDate));
    }
    public void setDepartmentComboListener(ActionListener listener) {
        departmentCombo.addActionListener(listener);
    }
    
    public void setRefreshButtonListener(ActionListener listener) {
        refreshButton.addActionListener(listener);
    }
    
    public void setBorrowButtonListener(ActionListener listener) {
        borrowButton.addActionListener(listener);
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

    public void setRequests(List<BorrowRequest> requests) {
        requestsTableModel.setRowCount(0);
        for (BorrowRequest request : requests) {
            Object[] row = {
                request.getId(),
                request.getBookTitle() != null ? request.getBookTitle() : "N/A",
                request.getRequestDate() != null ? request.getRequestDate() : "N/A",
                request.getReturnDate(),
                request.getStatus() != null ? request.getStatus().name() : "N/A",
                request.getApprovedAt() != null ? request.getApprovedAt() : "N/A"
            };
            requestsTableModel.addRow(row);
        }
    }
    
    public void setRefreshRequestsButtonListener(ActionListener listener) {
        refreshRequestsButton.addActionListener(listener);
    }
}





