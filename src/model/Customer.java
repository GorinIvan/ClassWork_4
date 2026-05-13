package model;

public class Customer extends User {
    private static final long serialVersionUID = 1L;
    private final String loyaltyCard;

    public Customer(String login, String passwordHash, String saltBase64, String fullName, String loyaltyCard) {
        super(login, passwordHash, saltBase64, fullName);
        this.loyaltyCard = loyaltyCard;
    }

    @Override
    public String getRole() {
        return "CUSTOMER";
    }

    @Override
    public String getGreeting() {
        return "Клиент " + getFullName() + ", карта: " + loyaltyCard;
    }

    @Override
    public void showMenu() {
        System.out.println("=== Меню клиента ===");
        System.out.println("1. Посмотреть мои заказы");
        System.out.println("2. Выйти");
    }
}
