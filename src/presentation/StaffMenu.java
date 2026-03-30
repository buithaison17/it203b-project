package presentation;

import services.StaffService;
import services.StaffServiceImpl;
import utils.InputMethod;

public class StaffMenu {
    private final StaffServiceImpl staffService = StaffServiceImpl.getInstance();

    public void showMenu() {
        int choice;
        do {
            System.out.println("1. Xem danh sách đặt máy");
            System.out.println("2. Cập nhật trạng thái đặt đồ ăn");
            System.out.println("0. Thoát");
            choice = InputMethod.getIntegerPositive("Nhập chức năng: ");
            switch (choice) {
                case 1:
                    staffService.viewListBooking();
                    break;
                case 2:
                    staffService.updateStatusBooking();
                    break;
                case 0:
                    break;
                default:
                    break;
            }
        } while (choice != 0);
    }
}
