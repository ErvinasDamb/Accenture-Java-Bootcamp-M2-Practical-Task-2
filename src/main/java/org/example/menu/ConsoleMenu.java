package org.example.menu;

import org.example.config.AppConfig;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.PaymentResult;
import org.example.payment.PaymentMethod;
import org.example.payment.PaymentMethodFactory;
import org.example.payment.PaymentProcessor;

import java.util.Scanner;

public class ConsoleMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final PaymentProcessor paymentProcessor = new PaymentProcessor();
    private final java.util.List<Order> orderHistory = new java.util.ArrayList<>();

    private Order currentOrder;

    public void start(){
        AppConfig config = AppConfig.getInstance();
        System.out.println("Welcome to " + config.getApplicationName());

        boolean running = true;
        while(running){
            printMenu();

            try {
                int option = Integer.parseInt(scanner.nextLine());

                switch (option){
                    case 1 -> createOrder();
                    case 2 -> addItem();
                    case 3 -> viewOrder();
                    case 4 -> payOrder();
                    case 5 -> viewHistory();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid option");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number for the menu option.");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private void createOrder(){
        System.out.println("Customer name:");
        String customerName = scanner.nextLine();

        currentOrder = Order.builder()
                .customerName(customerName)
                .build();
        System.out.println("Order created for " + customerName);
    }

    private void addItem(){
        if (currentOrder == null) {
            System.out.println("Please create an order first.");
            return;
        }

        System.out.println("Item name:");
        String itemName = scanner.nextLine();

        double price = readDouble("Price:");
        int quantity = readInt("Quantity:");

        currentOrder.addItem(new OrderItem(itemName, price, quantity));
        System.out.println("Item added to order");
    }

    private void viewOrder(){
        if (currentOrder == null) {
            System.out.println("No active order.");
            return;
        }

        System.out.println("Customer: " + currentOrder.getCustomerName());
        System.out.println("Status: " +  currentOrder.getStatus());
        System.out.println("Items:");

        for (OrderItem item : currentOrder.getItems()){
            System.out.println("- " + item);
        }

        System.out.println("Total: " + currentOrder.calculateTotal());
    }

    private void payOrder(){
        if (currentOrder == null) {
            System.out.println("No active order to pay.");
            return;
        }

        System.out.println("""
                Select payment method:
                1. Credit Card
                2. PayPal
                3. Gift Card
                4. Crypto
                """);
        int option;
        try {
            option = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }

        PaymentMethod paymentMethod;
        try {
            paymentMethod = switch(option){
                case 1 -> createCreditCardPayment();
                case 2 -> createPaypalPayment();
                case 3 -> createGiftCardPayment();
                case 4 -> createCryptoPayment();
                default -> {
                    System.out.println("Invalid payment method option.");
                    yield null;
                }
            };
        } catch (Exception e) {
            System.out.println("Failed to create payment method: " + e.getMessage());
            return;
        }

        if (paymentMethod == null) return;

        PaymentResult result = paymentProcessor.process(currentOrder, paymentMethod);
        System.out.println(result.getMessage());

        if (result.isSuccessful()) {
            orderHistory.add(currentOrder);
            currentOrder = null;
        }
    }

    private PaymentMethod createCreditCardPayment(){
        System.out.println("Card number:");
        String cardNumber =  scanner.nextLine();

        System.out.println("Card holder name:");
        String cardHolderName =  scanner.nextLine();

        return PaymentMethodFactory.createCreditCardPayment(cardNumber,cardHolderName);
    }

    private  PaymentMethod createPaypalPayment(){
        System.out.println("Email:");
        String email = scanner.nextLine();
        return PaymentMethodFactory.createPaypalPayment(email);
    }

    private PaymentMethod createGiftCardPayment(){
        System.out.println("Gift card code:");
        String code = scanner.nextLine();

        double balance = readDouble("Balance:");

        return PaymentMethodFactory.createGiftCardPayment(code, balance);
    }

    private PaymentMethod createCryptoPayment(){
        System.out.println("Wallet address:");
        String walletAddress = scanner.nextLine();
        return PaymentMethodFactory.createCryptoPayment(walletAddress);
    }

    private void viewHistory(){
        if (orderHistory.isEmpty()) {
            System.out.println("No completed orders.");
            return;
        }
        System.out.println("Order History:");
        for (int i = 0; i < orderHistory.size(); i++) {
            Order order = orderHistory.get(i);
            System.out.println((i + 1) + ". " + order.getCustomerName() + " - " + order.calculateTotal());
        }
    }

    private void printMenu(){
        System.out.println("""
                1. Create order
                2. Add item to order
                3. View order
                4. Pay order
                5. View history
                0. Exit
                """);
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.println(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine());
                if (value < 0) {
                    System.out.println("Value cannot be negative. Please try again.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.println(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value < 0) {
                    System.out.println("Value cannot be negative. Please try again.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }
}
