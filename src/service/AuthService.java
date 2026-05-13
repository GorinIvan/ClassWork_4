package service;

import model.Customer;
import model.Employee;
import model.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;

    private final Map<String, User> users = new HashMap<>();
    private final Map<String, String> passwordSalts = new HashMap<>();

    public boolean registerEmployee(String login, String password, String fullName, String department) {
        if (users.containsKey(login)) {
            return false;
        }
        String salt = generateSalt();
        users.put(login, new Employee(login, hashPassword(password, salt), fullName, department));
        passwordSalts.put(login, salt);
        return true;
    }

    public boolean registerCustomer(String login, String password, String fullName, String loyaltyCard) {
        if (users.containsKey(login)) {
            return false;
        }
        String salt = generateSalt();
        users.put(login, new Customer(login, hashPassword(password, salt), fullName, loyaltyCard));
        passwordSalts.put(login, salt);
        return true;
    }

    public User authorize(String login, String password) {
        User user = users.get(login);
        String salt = passwordSalts.get(login);
        if (user == null || salt == null || !user.getPasswordHash().equals(hashPassword(password, salt))) {
            return null;
        }
        return user;
    }

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private String hashPassword(String password, String saltBase64) {
        try {
            byte[] salt = Base64.getDecoder().decode(saltBase64);
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Password hashing failed", e);
        }
    }
}
