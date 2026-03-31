package models.dto;

public class ComputerStatistics {
    private int id;
    private String name;
    private int totalBook;
    private double totalAmount;

    public ComputerStatistics(int id, String name, int totalBook, double totalAmount) {
        this.id = id;
        this.name = name;
        this.totalBook = totalBook;
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

    public int getTotalBook() {
        return totalBook;
    }

    public void setTotalBook(int totalBook) {
        this.totalBook = totalBook;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return String.format("| %-8d | %-20s | %-12d | %-15.2f |",
                id,
                name,
                totalBook,
                totalAmount
        );
    }
}
