package services;

import dao.FoodDAOImpl;
import models.entity.Food;
import utils.AcceptChoice;
import utils.Config;
import utils.InputMethod;

import java.util.List;

public class FoodServiceImpl implements FoodService {
    private static FoodServiceImpl instance;
    private FoodDAOImpl foodDAO = FoodDAOImpl.getInstance();

    private FoodServiceImpl() {
    }

    public static FoodServiceImpl getInstance() {
        if (instance == null) {
            instance = new FoodServiceImpl();
        }
        return instance;
    }

    @Override
    public void saveFood() {
        String name = InputMethod.getStringAtLeastThreeCharacters("Nhập tên dồ ăn/ nước uống: ");
        String description = InputMethod.getString("Nhập mô tả: ");
        double price = InputMethod.getDoubleInteger("Nhập giá: ");
        int stock = InputMethod.getIntegerPositive("Nhập số lượng: ");
        boolean result = foodDAO.saveFood(name, description, price, stock);
        if (result) {
            System.out.printf("%s%s%s\n", Config.GREEN, "Thêm dồ ăn/ nước uống thành công", Config.RESET);
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Thêm dồ ăn/ nước uống thất bại", Config.RESET);
        }
    }

    @Override
    public void deleteFood() {
        int id = InputMethod.getIntegerPositive("Nhập ID dồ ăn/ nước uống: ");
        if (!AcceptChoice.accpect("Xác nhận xóa đồ ăn/ nước uống")) {
            System.out.printf("%s%s%s\n", Config.YELLOW, "Không có thay đổi", Config.RESET);
            return;
        }
        boolean result = foodDAO.deleteFood(id);
        if (result) {
            System.out.printf("%s%s%s\n", Config.GREEN, "Xóa dồ ăn/ nước uống thành công", Config.RESET);
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Xóa dồ ăn/ nước uống thất bại", Config.RESET);
        }
    }

    @Override
    public void updateFood() {
        int id = InputMethod.getIntegerPositive("Nhập ID đồ ăn/ đồ uống: ");
        Food food = foodDAO.findById(id);
        if (food == null) {
            System.out.printf("%s%s%s\n", Config.RED, "Đồ ăn/ nước uống không tồn tại", Config.RESET);
            return;
        }
        String name = InputMethod.getStringAtLeastThreeCharacters("Nhập tên mới (Enter để bỏ qua): ", food.getName());
        String description = InputMethod.getString("Nhập mô tả mới (Enter để bỏ qua): ", food.getDescription());
        double price = InputMethod.getDoubleInteger("Nhập giá mới (Enter để bỏ qua): ", food.getPrice());
        if (!AcceptChoice.accpect("Xác nhận cập nhật đồ ăn/ nước uống")) {
            System.out.printf("%s%s%s\n", Config.YELLOW, "Không có thay đổi", Config.RESET);
            return;
        }
        boolean result = foodDAO.updateFood(id, name, description, price);

        if (result) {
            System.out.printf("%s%s%s\n", Config.GREEN, "Cập nhật đồ ăn/ nước uống thành công", Config.RESET);
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Cập nhật đồ ăn/ nước uống thất bại", Config.RESET);
        }
    }

    @Override
    public void updateStock() {
        int id = InputMethod.getIntegerPositive("Nhập ID đồ ăn/ đồ uống: ");
        Food food = foodDAO.findById(id);
        if (food == null) {
            System.out.printf("%s%s%s\n", Config.RED, "Đồ ăn/ nước uống không tồn tại", Config.RESET);
            return;
        }
        int stock = InputMethod.getIntegerPositive("Nhập số lượng mới: ", food.getStock());
        if (!AcceptChoice.accpect("Xác nhận cập nhật số lượng đồ ăn/ nước uống")) {
            System.out.printf("%s%s%s\n", Config.YELLOW, "Không có thay đổi", Config.RESET);
            return;
        }
        boolean result = foodDAO.updateStock(id, stock);
        if (result) {
            System.out.printf("%s%s%s\n", Config.GREEN, "Cập nhật số lượng đồ ăn/ nước uống thành công", Config.RESET);
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Cập nhật số lượng đồ ăn/ nước uống thất bại", Config.RESET);
        }
    }

    @Override
    public void pagination() {
        int totalPage = foodDAO.getTotalPage();
        if (totalPage == 0) {
            System.out.printf("%s%s%s\n", Config.YELLOW, "Không có dữ liệu", Config.RESET);
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
            choice = InputMethod.getIntegerPositive("[1] Prev, [2] Next, [3].Exit");
            switch (choice) {
                case 1:
                    currentPage = currentPage > 1 ? currentPage - 1 : currentPage;
                    break;
                case 2:
                    currentPage = currentPage < totalPage ? currentPage + 1 : currentPage;
                    break;
                case 3:
                    break;
                default:
                    System.out.printf("%s%s%s\n", Config.RED, "Lựa chọn không hợp lệ", Config.RESET);
                    break;
            }
        } while (choice != 3);
    }
}
