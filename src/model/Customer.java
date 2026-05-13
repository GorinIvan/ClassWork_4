package model;

public class Customer extends User {
    private final String loyaltyCard;

    public Customer(String login, String password, String fullName, String loyaltyCard) {
        super(login, password, fullName);
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
