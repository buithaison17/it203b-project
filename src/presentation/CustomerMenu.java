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
            System.out.println("1. Đặt máy");
            System.out.println("2. Gọi món ăn");
            System.out.println("3. Xem thông tin cá nhân");
            System.out.println("4. Cập nhật thông tin");
            System.out.println("5. Đổi mật khẩu");
            System.out.println("6. Nạp tiền");
            System.out.println("7. Xem lịch sử đặt máy, gọi đồ ăn");
            System.out.println("0. Thoát");
            choice = InputMethod.getIntegerPositive("Nhập chức năng: ");
            switch (choice) {
                case 1:
                    userService.viewComputerCanBook();
                    break;
                case 2:
                    orderService.placeOrder();
                    break;
                case 3:
                    userService.viewProfile();
                    break;
                case 4:
                    break;
                case 5:
                    userService.changePassword();
                    break;
                case 6:
                    userService.rechargeMoney();
                    break;
                case 7:
                    break;
                default:
                    break;
            }
        } while (choice != 0);
    }
}
