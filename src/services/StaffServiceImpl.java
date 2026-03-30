package services;

import dao.BookingDAOImpl;
import dao.OrderDAOImpl;
import enums.OrderStatus;
import models.dto.BookingDTO;
import models.dto.OrderDTO;
import models.entity.Order;
import utils.Config;
import utils.InputMethod;
import utils.Validate;

import java.util.List;

public class StaffServiceImpl implements StaffService {
    private static StaffServiceImpl instance;
    private final BookingDAOImpl bookingDAO = BookingDAOImpl.getInstance();
    private final OrderDAOImpl orderDAO = OrderDAOImpl.getInstance();

    private StaffServiceImpl() {

    }

    public static StaffServiceImpl getInstance() {
        if (instance == null) {
            instance = new StaffServiceImpl();
        }
        return instance;
    }

    @Override
    public void viewListBooking() {
        int totalPage = bookingDAO.getTotalPage();
        if (totalPage == 0) {
            return;
        }

        int choice;
        int currentPage = 1;
        do {
            List<BookingDTO> bookingDTOS = bookingDAO.viewListBooking(currentPage);
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------------|");
            System.out.println("|                                                           DANH SÁCH ĐẶT MÁY                                                             |");
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------------|");
            System.out.printf("| %-10s | %-20s | %-30s | %-20s | %-15s | %-15s | %-15s |\n",
                    "ID", "Tên khách hàng", "Tên máy", "Thời gian bắt đầu", "Thời gian kết thúc", "Tổng tiền", "Ngày tạo");
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------------|");
            bookingDTOS.forEach(System.out::println);
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------------|");
            choice = InputMethod.getIntegerPositive("[1] Prev, [2] Next, [3].Exit");
            switch (choice) {
                case 1:
                    currentPage = currentPage > 1 ? currentPage - 1 : currentPage;
                    break;
                case 2:
                    currentPage = currentPage < totalPage ? currentPage + 1 : currentPage;
                    break;
                case 3:
                    break;
                default:
                    System.out.printf("%s%s%s\n", Config.RED, "Lựa chọn không hợp lệ", Config.RESET);
                    break;
            }
        } while (choice != 3);
    }

    private void update() {
        int orderId;
        String newStatus;
        while (true) {
            orderId = InputMethod.getIntegerPositive("Nhập mã đơn hàng: ");
            Order order = orderDAO.findById(orderId);
            if (order == null) {
                System.out.printf("%s%s%s\n", Config.RED, "Đơn hàng không tồn tại", Config.RESET);
            } else {
                break;
            }
        }
        while (true) {
            newStatus = InputMethod.getString("Nhập tình trạng mới (pending/preparing/served/cancelled): ");
            if (!Validate.validateOrderStatus(newStatus)) {
                System.out.printf("%s%s%s\n", Config.RED, "Tình trạng không hợp lệ", Config.RESET);
            } else {
                break;
            }
        }
        boolean result = orderDAO.updateStatusOrder(orderId, newStatus);
        if (result) {
            System.out.printf("%s%s%s\n", Config.GREEN, "Cập nhật tình trạng thành công", Config.RESET);
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Cập nhật tình trạng thất bại", Config.RESET);
        }
    }

    @Override
    public void updateStatusBooking() {
        int currentPage = 1;
        int choice;
        int totalPage = orderDAO.getTotalPage();
        if (totalPage == 0) {
            return;
        }
        // Hiển thị danh sách
        do {
            List<OrderDTO> orders = orderDAO.viewListOrder(currentPage);
            System.out.println("|-----------------------------------------------------------------------------------------------------------|");
            System.out.println("|                                                    CẬP NHẬT TÌNH TRẠNG                                        |");
            System.out.println("|-----------------------------------------------------------------------------------------------------------|");
            System.out.printf("| %-10s | %-20s | %-20s | %-15s | %-15s | %-15s |\n",
                    "ID", "Tên khách hàng", "Tình trạng", "Tổng tiền", "Ngày tạo", "Ngày hoàn thành");
            System.out.println("|-----------------------------------------------------------------------------------------------------------|");
            orders.forEach(System.out::println);
            System.out.println("|-----------------------------------------------------------------------------------------------------------|");
            choice = InputMethod.getIntegerPositive("[1]. Trang trước\n[2]. Trang sau\n[3]. Cập nhật trạng thái\n[4]. Thoát");
            switch (choice) {
                case 1:
                    currentPage = currentPage > 1 ? currentPage - 1 : currentPage;
                    break;
                case 2:
                    currentPage = currentPage < totalPage ? currentPage + 1 : currentPage;
                    break;
                case 3:
                    update();
                    break;
                case 4:
                    break;
                default:
                    System.out.printf("%s%s%s\n", Config.RED, "Lựa chọn không hợp lệ", Config.RESET);
                    break;
            }
        } while (choice != 4);
    }
}
