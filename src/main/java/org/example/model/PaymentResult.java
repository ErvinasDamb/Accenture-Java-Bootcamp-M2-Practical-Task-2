package org.example.model;

import lombok.Getter;

@Getter
public class PaymentResult {
    private final boolean successful;
    private final String message;

    public PaymentResult(boolean successful, String message){
        this.successful = successful;
        this.message = message;
    }
}
