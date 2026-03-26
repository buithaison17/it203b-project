package presentation;

import entity.User;
import services.AdminServiceImpl;
import utils.InputMethod;

import java.util.List;

public class AdminMenu {
    private AdminServiceImpl adminService = AdminServiceImpl.getInstance();

    public void viewAllUser() {
        List<User> users = adminService.findAll();
        for (User user : users) {
            System.out.println(user);
        }
    }

    public void showMenu() {
        int choice;
        do {
            System.out.println("1. Xem danh sách tài khoản");
            System.out.println("2. Thêm tài khoản");
            System.out.println("3. Xóa tài khoản");
            System.out.println("4. Phân quyền");
            System.out.println("5. Xem danh sách máy");
            System.out.println("7. Thêm máy tính");
            System.out.println("8. Sửa máy tính");
            System.out.println("9. Xóa máy tính");
            System.out.println("10. Xem danh sách món ăn");
            System.out.println("11. Thêm món ăn");
            System.out.println("12. Xóa món ăn");
            System.out.println("13. Sửa món ăn");
            System.out.println("14. Xem toàn bộ đơn hàng của hệ thống");
            System.out.println("15. Báo cáo thống kê");
            System.out.println("0. Thoát");
            choice = InputMethod.getIntegerPositive("Nhập chức năng: ");
            switch (choice) {
                case 1:
                    viewAllUser();
                    break;
            }
        } while (choice != 0);
    }
}
