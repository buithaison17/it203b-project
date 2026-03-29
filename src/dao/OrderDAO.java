package dao;

import java.util.Map;

public interface OrderDAO {
    public boolean placeOrder(Map<Integer, Integer> orderItems, double totalPrice);
}
