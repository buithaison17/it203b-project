package services;

import dao.UserDAOImpl;
import exceptions.DuplicateEmailException;
import exceptions.InvalidEmailException;
import exceptions.InvalidRoleException;
import models.dto.UserDTO;
import utils.*;

import java.util.List;
import java.util.Scanner;

public class AdminServiceImpl implements AdminService {
    private static AdminServiceImpl instance;
    private final UserDAOImpl userDAO = UserDAOImpl.getInstance();

    private AdminServiceImpl() {
    }

    public static AdminServiceImpl getInstance() {
        if (instance == null) instance = new AdminServiceImpl();
        return instance;
    }

    @Override
    public void findAll() {
        List<UserDTO> userDTOList = userDAO.findAll();
        for (UserDTO userDTO : userDTOList) {
            System.out.println(userDTO);
        }
    }

    @Override
    public void saveUser() {
        String fullName = InputMethod.getString("Nhập tên người dùng: ");

        // Nhập email
        String email;
        while (true) {
            email = InputMethod.getString("Nhập email: ");
            try {
                Validate.validateEmailFormat(email);
                if (UserDAOImpl.getInstance().findByEmail(email) != null) {
                    throw new DuplicateEmailException("Email đã tồn tại");
                }
                break;
            } catch (InvalidEmailException | DuplicateEmailException exception) {
                System.out.printf("%s%s%s\n", Config.RED, exception.getMessage(), Config.RESET);
            }
        }

        // Nhập mật khẩu
        String password = InputMethod.getString("Nhập mật khẩu: ");

        // Nhập Role
        String role;
        while (true) {
            System.out.print("Nhập vai trò (Customer/ Staff/ Admin): ");
            role = new Scanner(System.in).nextLine();
            if (role.isBlank()) {
                System.out.printf("%s%s%s\n", Config.RED, "Không được để trống", Config.RESET);
            }
            try {
                Validate.validateRole(role);
                break;
            } catch (InvalidRoleException exception) {
                System.out.printf("%s%s%s\n", Config.RED, exception.getMessage(), Config.RESET);
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
            System.out.print("Nhập vai trò (Customer/ Staff/ Admin): ");
            role = new Scanner(System.in).nextLine();
            try {
                Validate.validateRole(role);
                break;
            } catch (InvalidRoleException exception) {
                System.out.printf("%s%s%s\n", Config.RED, exception.getMessage(), Config.RESET);
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

    @Override
    public void generateUserReport() {

    }
}