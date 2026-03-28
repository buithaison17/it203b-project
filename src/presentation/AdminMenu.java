package presentation;

import services.*;
import utils.InputMethod;

public class AdminMenu {
    private final AdminServiceImpl adminService = AdminServiceImpl.getInstance();
    private final CategoryServiceImpl categoryService = CategoryServiceImpl.getInstance();
    private final ComputerServiceImpl computerService = ComputerServiceImpl.getInstance();
    private final FoodServiceImpl foodService = FoodServiceImpl.getInstance();

    public void showMenu() {
        int choice;
        do {
            System.out.println("1. Xem danh sách tài khoản");
            System.out.println("2. Thêm tài khoản");
            System.out.println("3. Xóa tài khoản");
            System.out.println("4. Phân quyền");
            System.out.println("5. Xem danh sách khu vực");
            System.out.println("6. Thêm khu vực");
            System.out.println("7. Xóa khu vực");
            System.out.println("8. Cập nhật khu vực");
            System.out.println("9. Xem danh sách máy");
            System.out.println("10. Thêm máy tính");
            System.out.println("11. Sửa máy tính");
            System.out.println("12. Xóa máy tính");
            System.out.println("13. Xem danh sách món ăn");
            System.out.println("14. Thêm món ăn");
            System.out.println("15. Xóa món ăn");
            System.out.println("16. Cập nhật thông tin món ăn");
            System.out.println("17. Cập nhật tồn kho");
            System.out.println("18. Xem toàn bộ đơn hàng của hệ thống");
            System.out.println("19. Báo cáo thống kê");
            System.out.println("0. Thoát");
            choice = InputMethod.getIntegerPositive("Nhập chức năng: ");
            switch (choice) {
                case 1:
                    adminService.findAll();
                    break;
                case 2:
                    adminService.saveUser();
                    break;
                case 3:
                    adminService.deleteUser();
                    break;
                case 4:
                    adminService.grantRole();
                    break;
                case 5:
                    categoryService.findAll();
                    break;
                case 6:
                    categoryService.addCategory();
                    break;
                case 7:
                    categoryService.deleteCategory();
                    break;
                case 8:
                    categoryService.updateCategory();
                    break;
                case 9:
                    computerService.findAll();
                    break;
                case 10:
                    computerService.addComputer();
                    break;
                case 11:
                    computerService.updateComputer();
                    break;
                case 12:
                    computerService.deleteComputer();
                    break;
                case 13:

                    break;
                case 14:
                    foodService.saveFood();
                    break;
                case 15:
                    foodService.deleteFood();
                    break;
                case 16:
                    foodService.updateFood();
                    break;
                case 17:
                    foodService.updateStock();
                    break;
                default:
                    break;
            }
        } while (choice != 0);
    }
}
