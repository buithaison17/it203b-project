package presentation;

import services.*;
import utils.Config;
import utils.InputMethod;

public class CustomerMenu {
    private final FoodServiceImpl foodService = FoodServiceImpl.getInstance();
    private final OrderServiceImpl orderService = OrderServiceImpl.getInstance();
    private final UserServiceImpl userService = UserServiceImpl.getInstance();
    private final BookingServiceImpl bookingService = BookingServiceImpl.getInstance();

    public void showCase6() {

        int choice;
        do {
            System.out.println("1. Xem lịch sử đặt máy");
            System.out.println("2. Xem lịch sử gọi đồ ăn");
            System.out.println("3. Thoát");
            choice = InputMethod.getIntegerPositive("Nhập chức năng: ");
            switch (choice) {
                case 1:
                    userService.showHistoryBooking();
                    break;
                case 2:
                    userService.showHistoryOrder();
                    break;
                case 3:
                    break;
                default:
                    System.out.printf("%s%s%s\n", Config.RED, "Chức năng không hợp lệ", Config.RESET);
                    break;
            }
        } while (choice != 3);
    }

    public void showMenu() {
        int choice;
        do {
            System.out.println("1. Đặt máy");
            System.out.println("2. Gọi món ăn");
            System.out.println("3. Xem thông tin cá nhân");
            System.out.println("4. Đổi mật khẩu");
            System.out.println("5. Nạp tiền");
            System.out.println("6. Xem lịch sử đặt máy, gọi đồ ăn");
            System.out.println("0. Thoát");
            choice = InputMethod.getIntegerPositive("Nhập chức năng: ");
            switch (choice) {
                case 1:
                    bookingService.viewComputerCanBook();
                    break;
                case 2:
                    orderService.placeOrder();
                    break;
                case 3:
                    userService.viewProfile();
                    break;
                case 4:
                    userService.changePassword();
                    break;
                case 5:
                    userService.rechargeMoney();
                    break;
                case 6:
                    showCase6();
                    break;
                case 0:
                    break;
                default:
                    break;
            }
        } while (choice != 0);
    }
}
