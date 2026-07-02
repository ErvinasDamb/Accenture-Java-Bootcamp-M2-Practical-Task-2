package org.example.model;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Order {
    private final String customerName;
    private final List<OrderItem> items;
    private OrderStatus status;
    private Discount discount = new NoDiscount();

    @Builder
    private Order(String customerName, List<OrderItem> items) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty");
        }
        this.customerName = customerName;
        this.items = items != null ? items : new ArrayList<>();
        this.status = OrderStatus.NEW;
    }

    public void addItem(OrderItem item){
        if (isPaid()) {
            throw new IllegalStateException("Cannot add items to a paid order");
        }
        items.add(item);
    }

    public double calculateTotal(){
        double subtotal = 0;
        for (OrderItem item : items) {
            subtotal += item.getPrice() * item.getQuantity();
        }
        double discountedSubtotal = discount.apply(subtotal);
        double tax = discountedSubtotal * org.example.config.AppConfig.getInstance().getTaxRate();
        return discountedSubtotal + tax;
    }

    public void markAsPaid(){
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot mark an empty order as paid");
        }
        this.status = OrderStatus.PAID;
    }

    public void applyDiscount(Discount discount){
        this.discount = discount;
    }

    public boolean isPaid(){
        return this.status == OrderStatus.PAID;
    }
}
