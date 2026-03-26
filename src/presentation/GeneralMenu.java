package presentation;

import enums.UserRole;
import services.UserServiceImpl;
import utils.Config;
import utils.InputMethod;

public class GeneralMenu {
    private final UserServiceImpl userService = UserServiceImpl.getInstance();
    private final CustomerMenu customerMenu = new CustomerMenu();
    private final AdminMenu adminMenu = new AdminMenu();
    private final StaffMenu staffMenu = new StaffMenu();

    public void processLogin() {
        String email = InputMethod.getString("Nhập email: ");
        String password = InputMethod.getString("Nhập mật khẩu: ");
        boolean result = userService.login(email, password);
        if (result) {
            System.out.printf("%s%s%s\n", Config.GREEN, "Đăng nhập thành công", Config.RESET);
            // Kiểm tra role và điều hướng menu
            UserRole role = Enum.valueOf(UserRole.class, Config.getUser().getRole());
            switch (role) {
                case STAFF:
//                    staffMenu.showMenu();
                    break;
                case ADMIN:
                    adminMenu.showMenu();
                    break;
                case CUSTOMER:
                    customerMenu.showMenu();
                    break;
            }
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Đăng nhập thất bại", Config.RESET);
        }
    }

    public void processRegister() {
        String fullName = InputMethod.getString("Nhập họ và tên: ");
        String email = InputMethod.getString("Nhập email: ");
        String password = InputMethod.getString("Nhập mật khẩu: ");
        boolean result = userService.register(fullName, email, password);
        if (result) {
            System.out.printf("%s%s%s\n", Config.GREEN, "Đăng ký thành công", Config.RESET);
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Đăng ký thất bại", Config.RESET);
        }
    }

    public void showMenu() {
        int choice;
        do {
            System.out.println("----- Cyber Center Management System -----");
            System.out.println("1. Đăng ký");
            System.out.println("2. Đăng nhập");
            System.out.println("0. Thoát");
            choice = InputMethod.getIntegerPositive("Nhập chức năng: ");
            switch (choice) {
                case 1:
                    processRegister();
                    break;
                case 2:
                    processLogin();
                    break;
                case 0:
                    System.out.println("Thoát thành công");
                    break;
                default:
                    System.out.println("Chức năng không hợp lệ");
                    break;
            }
        } while (choice != 0);
    }
}
