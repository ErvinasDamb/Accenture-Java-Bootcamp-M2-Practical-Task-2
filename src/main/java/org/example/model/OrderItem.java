package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class OrderItem {
    private final String name;
    private final double price;
    private final int quantity;

    public double calculateTotal(){
        return price * quantity;
    }
}
