package org.example.payment;

import org.example.model.PaymentResult;

public class GiftCardPayment extends PaymentMethod{
    private final String code;
    private double balance;

    public GiftCardPayment(String code, double balance) {
        super("GiftCard");
        this.code = code;
        this.balance = balance;
    }

    @Override
    public PaymentResult processPayment(double amount){
        if (balance < amount) {
            return new PaymentResult(false, "Insufficient balance on gift card");
        }
        balance -= amount;
        return new PaymentResult(true, "Paid " + amount + " using gift card");
    }
}
