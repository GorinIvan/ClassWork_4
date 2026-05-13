package model;

public abstract class User {
    private final String login;
    private final String password;
    private final String fullName;

    protected User(String login, String password, String fullName) {
        this.login = login;
        this.password = password;
        this.fullName = fullName;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
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
