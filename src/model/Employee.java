package model;

public class Employee extends User {
    private final String department;

    public Employee(String login, String passwordHash, String saltBase64, String fullName, String department) {
        super(login, passwordHash, saltBase64, fullName);
        this.department = department;
    }

    @Override
    public String getRole() {
        return "EMPLOYEE";
    }

    @Override
    public String getGreeting() {
        return "Сотрудник " + getFullName() + ", отдел: " + department;
    }

    @Override
    public void showMenu() {
        System.out.println("=== Меню сотрудника ===");
        System.out.println("1. Посмотреть задачи отдела");
        System.out.println("2. Выйти");
    }
}
