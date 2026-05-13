import model.User;
import service.AuthService;

import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static final String ROLE_EMPLOYEE = "EMPLOYEE";
    private static final String ROLE_CUSTOMER = "CUSTOMER";

    public static void main(String[] args) {
        AuthService authService = new AuthService();
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println("\n=== Главное меню ===");
                System.out.println("1. Регистрация");
                System.out.println("2. Авторизация");
                System.out.println("3. Выход");
                System.out.print("Выберите пункт: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        register(authService, scanner);
                        break;
                    case "2":
                        login(authService, scanner);
                        break;
                    case "3":
                        running = false;
                        break;
                    default:
                        System.out.println("Неверный выбор.");
                }
            }
        }
    }

    private static void register(AuthService authService, Scanner scanner) {
        System.out.println("\nКого регистрируем?");
        System.out.println("1. Employee");
        System.out.println("2. Customer");
        System.out.print("Выберите роль: ");
        String role = scanner.nextLine();

        System.out.print("Логин: ");
        String login = scanner.nextLine();
        char[] password = readPassword(scanner);
        System.out.print("ФИО: ");
        String fullName = scanner.nextLine();

        boolean ok;
        try {
            if ("1".equals(role)) {
                System.out.print("Отдел: ");
                String department = scanner.nextLine();
                ok = authService.registerEmployee(login, password, fullName, department);
            } else if ("2".equals(role)) {
                System.out.print("Номер карты лояльности: ");
                String loyaltyCard = scanner.nextLine();
                ok = authService.registerCustomer(login, password, fullName, loyaltyCard);
            } else {
                System.out.println("Неизвестная роль.");
                return;
            }
        } finally {
            Arrays.fill(password, '\0');
        }

        System.out.println(ok ? "Регистрация успешна." : "Пользователь с таким логином уже существует.");
    }

    private static void login(AuthService authService, Scanner scanner) {
        System.out.print("Логин: ");
        String login = scanner.nextLine();
        char[] password = readPassword(scanner);

        User user;
        try {
            user = authService.authorize(login, password);
        } finally {
            Arrays.fill(password, '\0');
        }
        if (user == null) {
            System.out.println("Неверный логин или пароль.");
            return;
        }

        System.out.println(user.getGreeting());
        System.out.println("Роль: " + user.getRole());
        boolean loggedIn = true;
        while (loggedIn) {
            user.showMenu();
            System.out.print("Выберите пункт: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    if (ROLE_EMPLOYEE.equals(user.getRole())) {
                        System.out.println("Список задач отдела пока недоступен.");
                    } else if (ROLE_CUSTOMER.equals(user.getRole())) {
                        System.out.println("Список заказов пока недоступен.");
                    } else {
                        System.out.println("Неизвестная роль.");
                    }
                    break;
                case "2":
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Неверный выбор. Доступные варианты: 1 или 2.");
                    break;
            }
        }
    }

    private static char[] readPassword(Scanner scanner) {
        Console console = System.console();
        if (console != null) {
            return console.readPassword("Пароль: ");
        }
        System.out.print("Пароль: ");
        return scanner.nextLine().toCharArray();
    }
}
