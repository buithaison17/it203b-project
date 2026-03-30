package payment;

public interface PaymentStrategy {
    public boolean pay(int userId, double amount);
}
