package service;

import model.Customer;
import model.Employee;
import model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private final Map<String, User> users = new HashMap<>();

    public boolean registerEmployee(String login, String password, String fullName, String department) {
        if (users.containsKey(login)) {
            return false;
        }
        users.put(login, new Employee(login, hashPassword(password), fullName, department));
        return true;
    }

    public boolean registerCustomer(String login, String password, String fullName, String loyaltyCard) {
        if (users.containsKey(login)) {
            return false;
        }
        users.put(login, new Customer(login, hashPassword(password), fullName, loyaltyCard));
        return true;
    }

    public User authorize(String login, String password) {
        User user = users.get(login);
        if (user == null || !user.getPasswordHash().equals(hashPassword(password))) {
            return null;
        }
        return user;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte b : hash) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
