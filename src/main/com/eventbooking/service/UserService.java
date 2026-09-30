package main.com.eventbooking.service;

import main.com.eventbooking.dao.UserDAO;
import main.com.eventbooking.daoimpl.UserDAOImpl;
import main.com.eventbooking.exception.UserNotFoundException;
import main.com.eventbooking.model.User;
import main.com.eventbooking.util.IDGenerator;

import java.util.List;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAOImpl();
    }

    public boolean registerUser(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new IllegalArgumentException(
                    "Password must contain at least 6 characters"
            );
        }

        if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be empty");
        }

        if (userDAO.getUserByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException(
                    "User with this email already exists"
            );
        }

        user.setUserId(IDGenerator.generateUserId());

        return userDAO.addUser(user);
    }

    public User login(String email, String password) {

        User user = userDAO.getUserByEmail(email);

        if (user == null) {
            throw new UserNotFoundException(
                    "No user found with email: " + email
            );
        }

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password");
        }

        return user;
    }

    public User getUserById(int userId) {

        User user = userDAO.getUserById(userId);

        if (user == null) {
            throw new UserNotFoundException(
                    "User not found with ID: " + userId
            );
        }

        return user;
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public boolean updateUser(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        getUserById(user.getUserId());

        return userDAO.updateUser(user);
    }

    public boolean deleteUser(int userId) {

        getUserById(userId);

        return userDAO.deleteUser(userId);
    }
}
