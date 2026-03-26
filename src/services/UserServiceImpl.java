package services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dao.UserDAOImpl;
import entity.User;
import exceptions.DuplicateEmailException;
import exceptions.InvalidEmailException;
import utils.Config;
import utils.HashUtil;
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
    public boolean register(String fullName, String email, String password) {
        // Validate dữ liệu đầu vào trước khi lưu vào CSDL
        // Kiểm tra email phải đúng định dạng
        // Email không được trùng
        try {
            Validate.validateEmailFormat(email);
            if (userDAO.findByEmail(email) != null) {
                throw new DuplicateEmailException("Email đã tồn tại");
            }
            String hashPassword = HashUtil.hashPassword(password);
            userDAO.saveUser(fullName, email, hashPassword);
            return true;
        } catch (InvalidEmailException | DuplicateEmailException exception) {
            System.out.printf("%s%s%s\n", Config.RED, exception.getMessage(), Config.RESET);
        }
        return false;
    }

    @Override
    public boolean login(String email, String password) {
        try {
            Validate.validateEmailFormat(email);
            User user = userDAO.findByEmail(email);
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
            if (result.verified) {
                Config.setUser(user);
                return true;
            }
        } catch (InvalidEmailException e) {
            System.out.printf("%s%s%s\n", Config.RED, e.getMessage(), Config.RESET);
        }
        return false;
    }
}
