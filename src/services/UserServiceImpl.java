package services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dao.UserDAOImpl;
import enums.UserRole;
import exceptions.DuplicateEmailException;
import models.entity.User;
import exceptions.InvalidEmailException;
import presentation.AdminMenu;
import presentation.CustomerMenu;
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
            try {
                // Đúng định dạng
                Validate.validateEmailFormat(email);
                // Check trùng
                if (userDAO.findByEmail(email) != null) {
                    throw new DuplicateEmailException("Email đã tồn tại");
                }
                break;
            } catch (InvalidEmailException | DuplicateEmailException exception) {
                System.out.printf("%s%s%s\n", Config.RED, exception.getMessage(), Config.RESET);
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
            try {
                Validate.validateEmailFormat(email);
                break;
            } catch (InvalidEmailException e) {
                System.out.printf("%s%s%s\n", Config.RED, e.getMessage(), Config.RESET);
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
        UserRole role = UserRole.valueOf(user.getRole());

        // Nếu tồn tại kiểm tra mật khẩu xem có khớp không
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
        if (result.verified) {
            // Lưu người dùng đang đăng nhập
            Config.setUser(user);
            // Điều hướng Menu theo role
            System.out.printf("%s%s%s\n", Config.GREEN, "Đăng nhập thành công", Config.RESET);
            switch (role) {
                case ADMIN -> new AdminMenu().showMenu();
//                case STAFF -> new StaffMenu().showMenu();
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
}
