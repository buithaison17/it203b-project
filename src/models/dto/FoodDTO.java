package models.dto;

public class FoodDTO {
    private int id;
    private String name;
    private int totalQuantitySell;
    private double totalAmount;

    public FoodDTO(int id, String name, int totalQuantitySell, double totalAmount) {
        this.id = id;
        this.name = name;
        this.totalQuantitySell = totalQuantitySell;
        this.totalAmount = totalAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalQuantitySell() {
        return totalQuantitySell;
    }

    public void setTotalQuantitySell(int totalQuantitySell) {
        this.totalQuantitySell = totalQuantitySell;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return String.format("| %-6d | %-20s | %-15d | %-15.2f |",
                id,
                name,
                totalQuantitySell,
                totalAmount
        );
    }
}
