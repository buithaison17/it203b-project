package payment;

import dao.UserDAOImpl;

public class MomoPayment implements PaymentStrategy {
    public final UserDAOImpl userDAO = UserDAOImpl.getInstance();

    @Override
    public boolean pay(int userId, double amount) {
        return userDAO.updateBalance(userId, amount);
    }
}
