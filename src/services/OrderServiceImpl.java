package services;

import dao.FoodDAOImpl;
import dao.OrderDAOImpl;
import models.entity.Food;
import utils.AcceptChoice;
import utils.Config;
import utils.InputMethod;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderServiceImpl implements OrderService {
    private final FoodDAOImpl foodDAO = FoodDAOImpl.getInstance();
    private Map<Integer, Integer> orderItems = new HashMap<>();
    private final OrderDAOImpl orderDAO = OrderDAOImpl.getInstance();
    private static OrderServiceImpl instance;

    private OrderServiceImpl() {
    }

    public static OrderServiceImpl getInstance() {
        if (instance == null) {
            instance = new OrderServiceImpl();
        }
        return instance;
    }

    private void chooseFood() {
        while (true) {
            int foodId = InputMethod.getIntegerPositive("Nhập ID món ăn (Bấm 0 để thoát): ");
            if (foodId == 0) return;
            Food food = foodDAO.findById(foodId);
            // Nếu nhập đúng ID
            if (food != null) {
                // Nhập số lượng muốn đặt
                int quantity = InputMethod.getIntegerPositive("Nhập số lượng muốn đặt: ");
                // Phải nhập lớn hơn 0
                if (quantity > 0) {
                    // Nếu đã thêm vào giỏ hàng trước đó lấy số lượng ra và cộng dồn
                    Integer beforeQuantity = orderItems.get(foodId);
                    if (beforeQuantity != null) {
                        quantity += beforeQuantity;
                    }
                    // Kiểm tra tồn kho
                    if (quantity <= food.getStock()) {
                        // Thêm vào giỏ hàng
                        orderItems.put(foodId, quantity);
                    } else {
                        System.out.printf("%s%s%s\n", Config.RED, "Số lượng vượt quá tồn kho", Config.RESET);
                    }
                } else {
                    System.out.printf("%s%s%s\n", Config.RED, "Số lượng phải lớn hơn 0", Config.RESET);
                }
            } else {
                System.out.printf("%s%s%s\n", Config.RED, "Món ăn không tồn tại", Config.RESET);
            }
        }
    }

    private void pay() {
        if (orderItems.isEmpty()) {
            System.out.printf("%s%s%s\n", Config.YELLOW, "Giỏ hàng trống", Config.RESET);
            return;
        }
        // Hiển thị danh sách món ăn đã thêm vào giỏ hàng
        System.out.println("|-----------------------------------------------------------|");
        System.out.printf("| %-10s | %-8s | %-15s | %-15s |\n", "Tên món ăn", "Số lượng", "Đơn giá", "Thành tiền");
        System.out.println("|-----------------------------------------------------------|");
        double totalPrice = 0;
        for (Map.Entry<Integer, Integer> entry : orderItems.entrySet()) {
            Food food = foodDAO.findById(entry.getKey());
            double price = entry.getValue() * food.getPrice();
            System.out.printf("| %-10s | %-8d | %-15.2f | %-15.2f |\n", food.getName(), entry.getValue(), food.getPrice(), price);
            System.out.println("|-----------------------------------------------------------|");
            totalPrice += price;
        }

        System.out.printf("Tổng tiền: %.2f\n", totalPrice);
        System.out.println("|-----------------------------------------------------------|");

        if (!AcceptChoice.accpect("Xác nhận thanh toán")) {
            return;
        }

        // Kiểm tra số dư trước khi thanh toán
        if (Config.getUser().getBalance() < totalPrice) {
            System.out.printf("%s%s%s\n", Config.RED, "Số dư không đủ", Config.RESET);
            return;
        }

        // Thanh toán
        boolean result = orderDAO.placeOrder(orderItems, totalPrice);
        if (result) {
            System.out.printf("%s%s%s\n", Config.GREEN, "Đặt đơn hàng thành công", Config.RESET);
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Đặt đơn hàng thất bại", Config.RESET);
        }
    }

    @Override
    public void placeOrder() {
        int totalPage = foodDAO.getTotalPage();
        if (totalPage == 0) {
            System.out.printf("%s%s%s\n", Config.YELLOW, "Danh sách trống", Config.RESET);
            return;
        }
        int choice = 1;
        int currentPage = 1;
        do {
            // Hiển thị bảng
            System.out.println("|-----------------------------------------------------------------------------------------------------------------|");
            System.out.println("|                                        DANH SÁCH ĐỒ ĂN/ NƯỚC UỐNG                                               |");
            System.out.println("|-----------------------------------------------------------------------------------------------------------------|");
            System.out.printf("| %-8s | %-20s | %-30s | %-10s | %-8s | %-20s |\n", "ID", "Tên", "Mô tả", "Giá", "Số lượng", "Ngày tạo");
            System.out.println("|-----------------------------------------------------------------------------------------------------------------|");
            List<Food> foods = foodDAO.findAll(currentPage);
            foods.forEach(System.out::println);
            System.out.println("|-----------------------------------------------------------------------------------------------------------------|");
            choice = InputMethod.getIntegerPositive("[1].Trang trước\n[2].Trang sau\n[3].Chọn món ăn\n[4]. Thanh toán\n[5]. Thoát");
            switch (choice) {
                case 1:
                    currentPage = currentPage > 1 ? currentPage - 1 : currentPage;
                    break;
                case 2:
                    currentPage = currentPage < totalPage ? currentPage + 1 : currentPage;
                    break;
                case 3:
                    chooseFood();
                    break;
                case 4:
                    pay();
                    break;
                case 5:
                    break;
                default:
                    System.out.printf("%s%s%s\n", Config.RED, "Lựa chọn không hợp lệ", Config.RESET);
                    break;
            }
        } while (choice != 5);
    }
}
