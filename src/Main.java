import model.User;
import service.AuthService;
import service.BinaryStorageService;

import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static final String ROLE_EMPLOYEE = "EMPLOYEE";
    private static final String ROLE_CUSTOMER = "CUSTOMER";
    private static final String DATA_FILE = "users.dat";
    private static final long AUTO_SAVE_INTERVAL_SECONDS = 30;

    public static void main(String[] args) {
        BinaryStorageService storage = new BinaryStorageService(DATA_FILE);
        AuthService authService = new AuthService();

        // Load saved data from binary file
        authService.setUsers(storage.load());
        System.out.println("Данные загружены из файла.");

        // Start background auto-save
        storage.startAutoSave(authService::getUsers, AUTO_SAVE_INTERVAL_SECONDS);

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println("\n=== Главное меню ===");
                System.out.println("1. Регистрация");
                System.out.println("2. Авторизация");
                System.out.println("3. Удалить пользователя");
                System.out.println("4. Отменить последнее действие");
                System.out.println("5. Сохранить данные");
                System.out.println("6. Выход");
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
                        deleteUser(authService, scanner);
                        break;
                    case "4":
                        System.out.println(authService.undo());
                        break;
                    case "5":
                        storage.save(authService.getUsers());
                        System.out.println("Данные сохранены.");
                        break;
                    case "6":
                        running = false;
                        break;
                    default:
                        System.out.println("Неверный выбор.");
                }
            }
        }

        // Save data and stop auto-save on exit
        storage.save(authService.getUsers());
        storage.stopAutoSave();
        System.out.println("Данные сохранены. До свидания!");
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

    private static void deleteUser(AuthService authService, Scanner scanner) {
        System.out.print("Логин пользователя для удаления: ");
        String login = scanner.nextLine();
        boolean ok = authService.deleteUser(login);
        System.out.println(ok ? "Пользователь удалён." : "Пользователь с таким логином не найден.");
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

