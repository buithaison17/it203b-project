package presentation;

import services.FoodServiceImpl;
import services.OrderServiceImpl;
import services.UserServiceImpl;
import utils.InputMethod;

public class CustomerMenu {
    private final FoodServiceImpl foodService = FoodServiceImpl.getInstance();
    private final OrderServiceImpl orderService = OrderServiceImpl.getInstance();
    private final UserServiceImpl userService = UserServiceImpl.getInstance();

    public void showMenu() {
        int choice;
        do {
            System.out.println("1. Xem danh sách máy trống");
            System.out.println("2. Đặt máy");
            System.out.println("3. Gọi món ăn");
            System.out.println("4. Xem thông tin cá nhân");
            System.out.println("5. Cập nhật thông tin");
            System.out.println("6. Đổi mật khẩu");
            System.out.println("7. Nạp tiền");
            System.out.println("8. Xem số dư");
            System.out.println("9. Xem lịch sử đặt máy, gọi đồ ăn");
            System.out.println("0. Thoát");
            choice = InputMethod.getIntegerPositive("Nhập chức năng: ");
            switch (choice) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    orderService.placeOrder();
                    break;
                case 6:
                    userService.changePassword();
                    break;
                default:
                    break;
            }
        } while (choice != 0);
    }
}
