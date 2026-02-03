package com.library.model;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private Department department;
    private boolean isAvailable;
    private int quantity;

    public Book() {
        this.quantity = 1;
    }

    public Book(int id, String title, String author, String isbn, Department department, boolean isAvailable,
            int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.department = department;
        this.isAvailable = isAvailable;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public boolean isAvailable() {
        return quantity > 0;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return title + " by " + (author != null ? author : "Unknown");
    }
}
