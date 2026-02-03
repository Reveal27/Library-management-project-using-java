package com.library.dao;

import com.library.model.User;
import java.util.List;

public interface IUserDAO {
    User authenticate(String username, String password);
    boolean createUser(User user);
    User getUserById(int id);
    User getUserByUsername(String username);
    List<User> getAllStudents();
    boolean updateUser(User user);
    boolean deleteUser(int id);
}






