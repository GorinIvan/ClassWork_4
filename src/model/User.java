package model;

public abstract class User {
    private final String login;
    private final String passwordHash;
    private final String saltBase64;
    private final String fullName;

    protected User(String login, String passwordHash, String saltBase64, String fullName) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.saltBase64 = saltBase64;
        this.fullName = fullName;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getSaltBase64() {
        return saltBase64;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGreeting() {
        return "Здравствуйте, " + fullName + "!";
    }

    public abstract String getRole();

    public abstract void showMenu();
}
