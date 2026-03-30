package services;

import dao.BookingDAOImpl;
import dao.ComputerDAOImpl;
import models.dto.ComputerDTO;
import models.entity.Computer;
import utils.AcceptChoice;
import utils.Config;
import utils.InputMethod;
import utils.Validate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class BookingServiceImpl implements BookingService {
    private static BookingServiceImpl instance;
    private final BookingDAOImpl bookingDAO = BookingDAOImpl.getInstance();
    private final ComputerDAOImpl computerDAO = ComputerDAOImpl.getInstance();

    private BookingServiceImpl() {
    }

    public static BookingServiceImpl getInstance() {
        if (instance == null) {
            instance = new BookingServiceImpl();
        }
        return instance;
    }

    private void chooseComputer(LocalDateTime startTime, LocalDateTime endTime) {
        while (true) {
            int computerId = InputMethod.getIntegerPositive("Nhập ID máy: ");
            Computer computer = computerDAO.findComputerById(computerId);
            if (computer == null) {
                System.out.printf("%s%s%s\n", Config.RED, "Máy không tồn tại", Config.RESET);
            } else {
                // Tính tổng tiền xác yêu cầu người dùng thanh toán
                double totalHour = Duration.between(startTime, endTime).toHours();
                double totalMoney = totalHour * computer.getPricePerHours();
                System.out.printf("Tổng tiền: %.2f\n", totalMoney);
                if (!AcceptChoice.accpect("Xác nhận thanh toán")) {
                    return;
                }

                // Kiểm tra số dư trước khi thanh toán
                if (totalMoney > Config.getUser().getBalance()) {
                    System.out.printf("%s%s%s\n", Config.RED, "Số dư không đủ", Config.RESET);
                    return;
                }

                // Thanh toán khi đủ số dư
                boolean result = bookingDAO.bookingComputer(computerId, startTime, endTime, totalMoney);
                if (result) {
                    System.out.printf("%s%s%s\n", Config.GREEN, "Đặt máy thành công", Config.RESET);
                } else {
                    System.out.printf("%s%s%s\n", Config.RED, "Đặt máy thấ bại", Config.RESET);
                }
                return;
            }
        }
    }

    private void displayTable(LocalDateTime startTime, LocalDateTime endTime) {
        int currentPage = 1;
        int choice;
        int totalPage = bookingDAO.getListComputerCanBook(currentPage, startTime, endTime).size() / Config.ROW_PER_PAGE;
        do {
            System.out.println("|-------------------------------------------------------------------------------------------------|");
            System.out.println("|                                      Danh sách máy có thể đặt                                   |");
            System.out.println("|-------------------------------------------------------------------------------------------------|");
            System.out.printf("| %-8s | %-20s | %-30s | %-10s | %-15s |\n", "ID", "Tên máy", "Cấu hình", "Giá", "Khu vực");
            System.out.println("|-------------------------------------------------------------------------------------------------|");
            List<ComputerDTO> computerDTOS = bookingDAO.getListComputerCanBook(currentPage, startTime, endTime);
            computerDTOS.forEach(System.out::println);
            choice = InputMethod.getIntegerPositive("[1].Trang trước\n[2].Trang sau\n[3].Chọn máy\n[4]. Thoát");
            switch (choice) {
                case 1:
                    currentPage = currentPage > 1 ? currentPage - 1 : currentPage;
                    break;
                case 2:
                    currentPage = currentPage < totalPage ? currentPage + 1 : currentPage;
                    break;
                case 3:
                    chooseComputer(startTime, endTime);
                    break;
                case 4:
                    break;
                default:
                    break;
            }
        } while (choice != 4);
    }

    @Override
    public void viewComputerCanBook() {
        LocalDateTime startTime;
        LocalDateTime endTime;
        while (true) {
            startTime = InputMethod.inputDateTime("Nhập ngày bắt đầu (HH:mm dd-MM-yyyy): ");
            // Ngày bắt đầu phải lớn hơn ngày hiện tại
            if (!Validate.validateDateTimeMoreThanNow(startTime)) {
                System.out.printf("%s%s%s\n", Config.RED, "Ngày bắt đầu phải lớn hơn ngày hiện tại", Config.RESET);
            } else {
                break;
            }
        }
        while (true) {
            endTime = InputMethod.inputDateTime("Nhập ngày kết thúc (HH:mm dd-MM-yyyy): ");
            if (!Validate.validateStartTimeLessThanEndTime(startTime, endTime)) {
                System.out.printf("%s%s%s\n", Config.RED, "Ngày kết thúc phải lớn hơn ngày bắt đầu", Config.RESET);
            } else {
                break;
            }
        }

        // Hiển thị danh sách
        displayTable(startTime, endTime);
    }
}
