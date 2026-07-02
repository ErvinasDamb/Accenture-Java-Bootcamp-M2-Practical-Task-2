# M2 Practical Task: Payment Processing Implementation Summary

This implementation completes the Payment Processing Console App with the following features:

## Core Requirements (Completed)
- **Payment Methods:** Fully implemented `CreditCardPayment`, `PaypalPayment`, and `GiftCardPayment`.
    - **Credit Card:** Validates card number length and holder name.
    - **PayPal:** Integrated with email-based payment flow.
    - **Gift Card:** Implemented balance tracking and sufficiency checks.
- **Console Menu:** A fully functional interactive menu for creating orders, adding items, viewing summaries, and processing payments.
- **Factory Pattern:** Used `PaymentMethodFactory` to encapsulate the creation of different payment strategies.

## Technical Enhancements
- **Lombok Integration:** Applied `@Getter`, `@Builder`, `@AllArgsConstructor`, and `@ToString` across model classes (`Order`, `OrderItem`, `AppConfig`, `PaymentResult`) to eliminate boilerplate code.
- **Design Patterns:** 
    - **Singleton:** Implemented for `AppConfig` to manage global settings like tax rates.
    - **Template Method:** Used in the abstract `PaymentMethod` class to standardize the payment process.
- **Defensive Programming:** Added state validations in `Order` (e.g., preventing modifications to paid orders) and robust error handling in `PaymentProcessor`.

## Stretch Goals Added
- **New Payment Method:** Added `CryptoPayment` support.
- **Tax Calculation:** Integrated automatic 21% tax calculation via `AppConfig`.
- **Order History:** Implemented in-memory storage to track and view all completed orders.
- **Input Validation:** Enhanced `ConsoleMenu` with safe input reading (`readDouble`, `readInt`) to handle invalid formats and non-negative constraints.
