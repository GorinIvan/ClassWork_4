package service;

import model.Customer;
import model.Employee;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private final Map<String, User> users = new HashMap<>();

    public boolean registerEmployee(String login, String password, String fullName, String department) {
        if (users.containsKey(login)) {
            return false;
        }
        users.put(login, new Employee(login, password, fullName, department));
        return true;
    }

    public boolean registerCustomer(String login, String password, String fullName, String loyaltyCard) {
        if (users.containsKey(login)) {
            return false;
        }
        users.put(login, new Customer(login, password, fullName, loyaltyCard));
        return true;
    }

    public User authorize(String login, String password) {
        User user = users.get(login);
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }
        return user;
    }
}
