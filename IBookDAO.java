package com.library.dao;

import com.library.model.Book;
import com.library.model.Department;
import java.util.List;

public interface IBookDAO {
    boolean addBook(Book book);
    boolean removeBook(int bookId);
    Book getBookById(int id);
    List<Book> getAllBooks();
    List<Book> getBooksByDepartment(Department department);
    List<Book> getAvailableBooksByDepartment(Department department);
    boolean updateBookAvailability(int bookId, boolean isAvailable);
    boolean updateBook(Book book);
}






