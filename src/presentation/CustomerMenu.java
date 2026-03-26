package presentation;

import utils.InputMethod;

public class CustomerMenu {
    public void showMenu() {
        int choice;
        do {
            System.out.println("1. Xem danh sách máy trống");
            System.out.println("2. Đặt máy");
            System.out.println("3. Xem menu món ăn");
            System.out.println("4. Gọi món ăn");
            System.out.println("5. Xem thông tin cá nhân");
            System.out.println("6. Cập nhật thông tin");
            System.out.println("7. Đổi mật khẩu");
            System.out.println("8. Nạp tiền");
            System.out.println("9. Xem số dư");
            System.out.println("10. Xem lịch sử giao dịch");
            System.out.println("0. Thoát");
            choice = InputMethod.getIntegerPositive("Nhập chức năng: ");
            switch (choice) {
                case 1:
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        } while (choice != 0);
    }
}
