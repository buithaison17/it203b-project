package services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dao.UserDAOImpl;
import enums.UserRole;
import models.dto.UserDTO;
import models.entity.User;
import presentation.AdminMenu;
import presentation.CustomerMenu;
import presentation.StaffMenu;
import utils.Config;
import utils.HashUtil;
import utils.InputMethod;
import utils.Validate;

public class UserServiceImpl implements UserService {
    private static UserServiceImpl instance;
    private final UserDAOImpl userDAO = UserDAOImpl.getInstance();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        if (instance == null) instance = new UserServiceImpl();
        return instance;
    }

    @Override
    public void register() {
        String fullName = InputMethod.getString("Nhập họ và tên: ");

        String email;
        // Validate Email
        while (true) {
            email = InputMethod.getString("Nhập email: ");
            // Đúng định dạng
            if (!Validate.validateEmailFormat(email)) {
                System.out.printf("%s%s%s\n", Config.RED, "Email không đúng định dạng", Config.RESET);
            }
            // Check trùng
            else if (userDAO.findByEmail(email) != null) {
                System.out.printf("%s%s%s", Config.RED, "Email đã tồn tại", Config.RESET);
            } else {
                break;
            }
        }

        String password = InputMethod.getString("Nhập mật khẩu: ");
        String hashPassword = HashUtil.hashPassword(password);
        boolean result = userDAO.saveUser(fullName, email, hashPassword);
        if (result) {
            System.out.printf("%s%s%s\n", Config.GREEN, "Đăng ký thành công", Config.RESET);
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Đăng ký thất bại", Config.RESET);
        }
    }

    @Override
    public void login() {
        String email;
        while (true) {
            email = InputMethod.getString("Nhập Email: ");
            if (!Validate.validateEmailFormat(email)) {
                System.out.printf("%s%s%s\n", Config.RED, "Email không đúng định dạng", Config.RESET);
            } else {
                break;
            }
        }

        String password = InputMethod.getString("Nhập mật khẩu: ");

        // Kiểm tra người dùng tồn tại
        User user = userDAO.findByEmail(email);
        if (user == null) {
            System.out.printf("%s%s%s\n", Config.RED, "Email hoặc mật khẩu không chính xác", Config.RESET);
            return;
        }

        // Lấy role để điều hướng menu
        UserRole role = UserRole.valueOf(user.getRole().toUpperCase());

        // Nếu tồn tại kiểm tra mật khẩu xem có khớp không
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
        if (result.verified) {
            // Lưu người dùng đang đăng nhập
            Config.setUser(user);
            // Điều hướng Menu theo role
            System.out.printf("%s%s%s\n", Config.GREEN, "Đăng nhập thành công", Config.RESET);
            switch (role) {
                case ADMIN -> new AdminMenu().showMenu();
                case STAFF -> new StaffMenu().showMenu();
                case CUSTOMER -> new CustomerMenu().showMenu();
            }
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Email hoặc mật khẩu không chính xác", Config.RESET);
        }

    }

    @Override
    public void changePassword() {
        String oldPassword = InputMethod.getString("Nhập mật khẩu cũ: ");
        String newPassword = InputMethod.getString("Nhập mật khẩu mới: ");
        String confirmPassword = InputMethod.getString("Nhập lại mật khẩu mới: ");

        String hashNewPassword = HashUtil.hashPassword(newPassword);
        String hashConfirmPassword = HashUtil.hashPassword(confirmPassword);

        // So sánh mật khẩu cũ
        BCrypt.Result resultMatchOldPassword = BCrypt.verifyer().verify(oldPassword.toCharArray(), Config.getUser().getPassword());
        if (!resultMatchOldPassword.verified) {
            System.out.printf("%s%s%s\n", Config.RED, "Mật khẩu cũ không chính xác", Config.RESET);
            return;
        }

        // Kiểm tra mật khẩu khớp
        if (!newPassword.equals(confirmPassword)) {
            System.out.printf("%s%s%s\n", Config.RED, "Mật khẩu không khớp", Config.RESET);
            return;
        }

        // Đổi mật khẩu
        Config.getUser().setPassword(hashNewPassword);
        boolean result = userDAO.changePassword(Config.getUser().getId(), hashNewPassword);
        if (result) {
            System.out.printf("%s%s%s\n", Config.GREEN, "Đổi mật khẩu thành công", Config.RESET);
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Đổi mật khẩu thất bại", Config.RESET);
        }
    }

    private void paymentByMoMo() {
        double amount;
        while (true) {
            amount = InputMethod.getDoubleInteger("Nhập số tiền: ");
            if (amount == 0) {
                System.out.printf("%s%s%s\n", Config.RED, "Số tiền phải lớn hơn 0", Config.RESET);
            } else {
                break;
            }
        }
        boolean result = userDAO.updateBalance(Config.getUser().getId(), amount);
        if (result) {
            System.out.printf("%s%s%s\n", Config.GREEN, "Thanh toán thành công bằng MoMo", Config.RESET);
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Thanh toán thất bại", Config.RESET);
        }
    }

    private void paymentByMBBank() {
        double amount;
        while (true) {
            amount = InputMethod.getDoubleInteger("Nhập số tiền: ");
            if (amount == 0) {
                System.out.printf("%s%s%s\n", Config.RED, "Số tiền phải lớn hơn 0", Config.RESET);
            } else {
                break;
            }
        }
        boolean result = userDAO.updateBalance(Config.getUser().getId(), amount);
        if (result) {
            System.out.printf("%s%s%s\n", Config.GREEN, "Thanh toán thành công bằng MBBank", Config.RESET);
        } else {
            System.out.printf("%s%s%s\n", Config.RED, "Thanh toán thất bại", Config.RESET);
        }
    }

    @Override
    public void rechargeMoney() {
        int choice;
        do {
            System.out.println("1. MoMo");
            System.out.println("2. MBBank");
            System.out.println("3. Thoát");
            choice = InputMethod.getIntegerPositive("Chọn phương thức thanh toán: ");
            switch (choice) {
                case 1:
                    paymentByMoMo();
                    return;
                case 2:
                    paymentByMBBank();
                    return;
                case 3:
                    break;
                default:
                    System.out.printf("%s%s%s\n", Config.YELLOW, "Chức năng không hợp lệ", Config.RESET);
                    break;
            }
        } while (choice != 3);
    }

    @Override
    public void viewProfile() {
        System.out.println("----------------------- THÔNG TIN CÁ NHÂN -----------------------");
        UserDTO user = new UserDTO(
                Config.getUser().getId(),
                Config.getUser().getFullName(),
                Config.getUser().getEmail(),
                Config.getUser().getRole(),
                Config.getUser().getBalance(),
                Config.getUser().getCreatedAt());

        System.out.printf("| %-7s | %-15s | %-20s | %-10s |\n", "ID", "Họ tên", "Email", "Số dư");
        System.out.println(user.toStringNotCreatedAtAndRole());
        System.out.println("-----------------------------------------------------------------");
    }
}
