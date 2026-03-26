package entity;

public class OrderItem {
    private int id;
    private int orderId;
    private int foodId;
    private int quantity;

    public OrderItem(int id, int orderId, int foodId, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.foodId = foodId;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getFoodId() {
        return foodId;
    }

    public int getQuantity() {
        return quantity;
    }
}
