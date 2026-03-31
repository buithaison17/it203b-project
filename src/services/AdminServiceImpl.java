package services;

import dao.BookingDAOImpl;
import dao.OrderDAO;
import dao.OrderDAOImpl;
import dao.UserDAOImpl;
import models.dto.ComputerStatistics;
import models.dto.FoodDTO;
import models.dto.UserDTO;
import utils.*;

import java.util.List;

public class AdminServiceImpl implements AdminService {
    private static AdminServiceImpl instance;
    private final UserDAOImpl userDAO = UserDAOImpl.getInstance();
    private final OrderDAOImpl orderDAO = OrderDAOImpl.getInstance();
    private final BookingDAOImpl bookingDAO = BookingDAOImpl.getInstance();

    private AdminServiceImpl() {
    }

    public static AdminServiceImpl getInstance() {
        if (instance == null) instance = new AdminServiceImpl();
        return instance;
    }

    @Override
    public void findAll() {
        int totalPage = userDAO.getTotalPage();
        if (totalPage == 0) {
            System.out.printf("%s%s%s\n", Config.YELLOW, "Không có người dùng", Config.RESET);
            return;
        }
        int choice;
        int currentPage = 1;
        do {
            // Hiển thị bảng
            System.out.println("|---------------------------------------------------------------------------------------------------|");
            System.out.println("|                                       DANH SÁCH NGƯỜI DÙNG                                        |");
            System.out.println("|---------------------------------------------------------------------------------------------------|");
            System.out.printf("| %-7s | %-15s | %-20s | %-10s | %-10s | %-20s |\n", "ID", "Họ tên", "Email", "Vai trò", "Số dư", "Ngày tạo");
            System.out.println("|---------------------------------------------------------------------------------------------------|");
            List<UserDTO> userDTOS = userDAO.findAll(currentPage);
            userDTOS.forEach(System.out::println);
            System.out.println("|---------------------------------------------------------------------------------------------------|");
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

    @Override
    public void saveUser() {
        String fullName = InputMethod.getString("Nhập tên người dùng: ");

        // Nhập email
        String email;
        while (true) {
            email = InputMethod.getString("Nhập email: ");
            if (!Validate.validateEmailFormat(email)) {
                System.out.printf("%s%s%s\n", Config.RED, "Email không đúng định dạng", Config.RESET);
            } else if (UserDAOImpl.getInstance().findByEmail(email) != null) {
                System.out.printf("%s%s%s\n", Config.RED, "Email đã tồn tại", Config.RESET);
            } else {
                break;
            }
        }

        // Nhập mật khẩu
        String password = InputMethod.getString("Nhập mật khẩu: ");

        // Nhập Role
        String role;
        while (true) {
            role = InputMethod.getStringAtLeastThreeCharacters("Nhập vai trò (Customer/ Staff/ Admin): ");
            if (!Validate.validateRole(role)) {
                System.out.printf("%s%s%s\n", Config.RED, "Vai trò không hợp lệ", Config.RESET);
            } else {
                break;
            }
        }

        // Nhập số dư
        double balance = InputMethod.getDoubleInteger("Nhập số dư: ");

        // Mã hóa mật khẩu
        String hashPassword = HashUtil.hashPassword(password);

        // Lưu người dùng vào DB
        boolean result = userDAO.saveUser(fullName, email, hashPassword, role, balance);
        if (result) {
            System.out.printf("%s%s%s\n", Config.GREEN, "Tạo tài khoản thành công", Config.RESET);
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Tạo tài khoản thất bại", Config.RESET);
        }
    }

    @Override
    public void deleteUser() {
        int userId = InputMethod.getIntegerPositive("Nhập ID người dùng: ");
        if (Config.getUser().getId() == userId) {
            System.out.printf("%s%s%s\n", Config.RED, "Không thể xóa tài khoản đang đăng nhập", Config.RESET);
            return;
        }
        if (!AcceptChoice.accpect("Xác nhận xóa tài khoản: ")) {
            System.out.printf("%s%s%s\n", Config.YELLOW, "Không xóa tài khoản", Config.RESET);
            return;
        }
        boolean result = userDAO.deleteUser(userId);
        if (result) {
            System.out.printf("%s%s%s\n", Config.GREEN, "Xóa tài khoản thành công", Config.RESET);
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Xóa tài khoản thất bại", Config.RESET);
        }
    }

    @Override
    public void grantRole() {
        int id = InputMethod.getIntegerPositive("Nhập ID người dùng: ");
        UserDTO userDTO = userDAO.findById(id);
        // Kiểm tra người dùng tồn tại
        if (userDTO == null) {
            System.out.printf("%s%s%s\n", Config.RED, "Người dùng không tồn tại", Config.RESET);
            return;
        }
        // Nhập role và kiểm tra
        String role;
        while (true) {
            role = InputMethod.getStringAtLeastThreeCharacters("Nhập vai trò (admin/customer/staff): ");
            if (!Validate.validateRole(role)) {
                System.out.printf("%s%s%s\n", Config.RED, "Vai trò không hợp lệ", Config.RESET);
            } else {
                break;
            }
        }
        if (!AcceptChoice.accpect("Xác nhận thay đổi vai trò người dùng")) {
            System.out.printf("%s%s%s\n", Config.YELLOW, "Không thay đổi vai trò", Config.RESET);
            return;
        }
        // Gọi DB lưu dữ liệu
        boolean result = userDAO.grantRole(id, role);
        if (result) {
            System.out.printf("%s%s%s\n", Config.GREEN, "Cấp quyền thành công", Config.RESET);
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Cấp quyền thất bại", Config.RESET);
        }
    }

    private void showBestFood() {
        int totalPage = orderDAO.getTotalPageOfReport();
        if (totalPage == 0) {
            System.out.printf("%s%s%s\n", Config.RED, "Không có dữ liệu", Config.RESET);
            return;
        }
        int currentPage = 1;
        int choice;
        do {
            System.out.println("|-------------------------------------------------------------------|");
            System.out.println("|                        DANH SÁCH MÓN ĂN BÁN CHẠY                  |");
            System.out.println("|-------------------------------------------------------------------|");

            System.out.printf("| %-6s | %-20s | %-15s | %-15s |\n",
                    "ID", "Tên món ăn", "Số lượng đặt", "Tổng tiền");
            System.out.println("|-------------------------------------------------------------------|");
            List<FoodDTO> foodDTOS = orderDAO.viewListBestFood(currentPage);
            foodDTOS.forEach(System.out::println);
            System.out.println("|-------------------------------------------------------------------|");
            choice = InputMethod.getIntegerPositive("[1].Trang trước\n[2].Trang sau\n[3].Thoát");
            if (choice == 1) {
                currentPage = currentPage > 1 ? currentPage - 1 : currentPage;
            } else if (choice == 2) {
                currentPage = currentPage < totalPage ? currentPage + 1 : currentPage;
            }
        } while (choice != 3);
    }

    private void showBestComputer() {
        int totalPage = bookingDAO.getTotalPageListBestComputer();
        if (totalPage == 0) {
            System.out.printf("%s%s%s\n", Config.RED, "Không có dữ liệu", Config.RESET);
            return;
        }
        int currentPage = 1;
        int choice;
        do {
            System.out.println("|------------------------------------------------------------------|");
            System.out.println("|                 DANH SÁCH MÁY TÍNH ĐƯỢC ĐẶT NHIỀU NHẤT           |");
            System.out.println("|------------------------------------------------------------------|");

            System.out.printf("| %-8s | %-20s | %-12s | %-15s |\n",
                    "ID", "Tên máy tính", "Số lượng đặt", "Tổng tiền");
            System.out.println("|------------------------------------------------------------------|");
            List<ComputerStatistics> computerStatisticsList = bookingDAO.getListBestComputer(currentPage);
            computerStatisticsList.forEach(System.out::println);
            System.out.println("|-----------------------------------------------------------------|");
            choice = InputMethod.getIntegerPositive("[1].Trang trước\n[2].Trang sau\n[3].Thoát");
            switch (choice) {
                case 1:
                    currentPage = currentPage > 1 ? currentPage - 1 : currentPage;
                    break;
                case 2:
                    currentPage = currentPage < totalPage ? currentPage + 1 : currentPage;
                    break;
                case 0:
                    break;
                default:
                    break;
            }
        } while (choice != 3);
    }

    private void showRevenue() {
        double revenueBooking = bookingDAO.getRevenue();
        double revenueOrder = orderDAO.getRevenue();
        System.out.println("|------------------------------------------------|");
        System.out.println("|                     DOANH THU                  |");
        System.out.println("|------------------------------------------------|");
        System.out.printf("| %-28s | %-15s |\n", "Loại dịch vụ", "Doanh thu");
        System.out.println("|------------------------------------------------|");
        System.out.printf("| %-28s | %15s |\n", "Đặt máy tính", "$" + String.format("%,.2f", revenueBooking));
        System.out.printf("| %-28s | %15s |\n", "Đặt món ăn", "$" + String.format("%,.2f", revenueOrder));
        System.out.println("|---------------------------------------------- -|");
    }

    @Override
    public void generateUserReport() {
        int choice;
        do {
            System.out.println("1. Xếp hàng món ăn bán chạy");
            System.out.println("2. Xếp hàng máy tính được đặt nhiều nhất");
            System.out.println("3. Xem doanh thu");
            System.out.println("0. Thoát");
            choice = InputMethod.getIntegerPositive("Nhập chức năng: ");
            switch (choice) {
                case 1:
                    showBestFood();
                    break;
                case 2:
                    showBestComputer();
                    break;
                case 3:
                    showRevenue();
                    break;
                case 0:
                    break;
                default:
                    break;
            }
        } while (choice != 0);
    }
}