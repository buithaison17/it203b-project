package presentation;

import enums.UserRole;
import services.UserServiceImpl;
import utils.Config;
import utils.InputMethod;

public class GeneralMenu {
    private final UserServiceImpl userService = UserServiceImpl.getInstance();

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
                    userService.register();
                    break;
                case 2:
                    userService.login();
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
