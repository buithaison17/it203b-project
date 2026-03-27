package presentation;

import services.AdminServiceImpl;
import utils.InputMethod;

public class AdminMenu {
    private AdminServiceImpl adminService = AdminServiceImpl.getInstance();

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
                case 15:
                    adminService.generateUserReport();
                    break;
            }
        } while (choice != 0);
    }
}
