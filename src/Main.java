import model.User;
import service.AuthService;

import java.util.Scanner;

public class Main {
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
        System.out.print("Пароль: ");
        String password = scanner.nextLine();
        System.out.print("ФИО: ");
        String fullName = scanner.nextLine();

        boolean ok;
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

        System.out.println(ok ? "Регистрация успешна." : "Пользователь с таким логином уже существует.");
    }

    private static void login(AuthService authService, Scanner scanner) {
        System.out.print("Логин: ");
        String login = scanner.nextLine();
        System.out.print("Пароль: ");
        String password = scanner.nextLine();

        User user = authService.authorize(login, password);
        if (user == null) {
            System.out.println("Неверный логин или пароль.");
            return;
        }

        System.out.println(user.getGreeting());
        System.out.println("Роль: " + user.getRole());
        user.showMenu();
    }
}
