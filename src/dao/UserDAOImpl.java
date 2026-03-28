package dao;

import models.dto.UserDTO;
import models.entity.User;
import utils.Config;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private static UserDAOImpl instance;

    private UserDAOImpl() {
    }

    public static UserDAOImpl getInstance() {
        if (instance == null) instance = new UserDAOImpl();
        return instance;
    }

    // Lưu User
    @Override
    public boolean saveUser(String fullName, String email, String password) {
        String saveUserSql = "insert into  users(full_name, email, password) values (?,?,?)";
        try (Connection connection = new DatabaseConnection().getConnection(); PreparedStatement stmSaveUser = connection.prepareStatement(saveUserSql)) {
            stmSaveUser.setString(1, fullName);
            stmSaveUser.setString(2, email);
            stmSaveUser.setString(3, password);
            int result = stmSaveUser.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean saveUser(String fullname, String email, String password, String role, double balance) {
        String saveUserSql = "insert into users(full_name, email, password, role, balance) values (?, ?, ?, ?, ?)";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement stmSaveUser = connection.prepareStatement(saveUserSql)) {
            stmSaveUser.setString(1, fullname);
            stmSaveUser.setString(2, email);
            stmSaveUser.setString(3, password);
            stmSaveUser.setString(4, role);
            stmSaveUser.setDouble(5, balance);
            return stmSaveUser.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
            return false;
        }
    }

    // Tìm kiếm bằng email
    @Override
    public User findByEmail(String email) {
        User user = null;
        String findUserSql = "select user_id, full_name, email, password, role, balance, created_at from users where email = ?";
        try (Connection connection = new DatabaseConnection().getConnection(); PreparedStatement stmFindUser = connection.prepareStatement(findUserSql)) {
            stmFindUser.setString(1, email);
            ResultSet resultSet = stmFindUser.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String fullName = resultSet.getString("full_name");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                double balance = resultSet.getDouble("balance");
                LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                user = mapToUser(resultSet);
            }
        } catch (SQLException e) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return user;
    }

    // Chuyển ResultSet sang user
    private User mapToUser(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getInt("user_id"), resultSet.getString("full_name"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("role"), resultSet.getDouble("balance"), resultSet.getTimestamp("created_at").toLocalDateTime());
    }

    // Chuyển ResultSet sang UserDTO
    private UserDTO maptoUserDTO(ResultSet resultSet) throws SQLException {
        return new UserDTO(resultSet.getInt("user_id"), resultSet.getString("full_name"), resultSet.getString("email"), resultSet.getString("role"), resultSet.getDouble("balance"), resultSet.getTimestamp("created_at").toLocalDateTime());
    }

    // Lấy danh sách user
    @Override
    public List<UserDTO> findAll(int currentPage) {
        List<UserDTO> userDTOS = new ArrayList<>();
        String findAllSql = "select user_id, full_name, email, role, balance, created_at from users limit ? offset ?";
        try (Connection connection = new DatabaseConnection().getConnection(); PreparedStatement stmFindAll = connection.prepareStatement(findAllSql)) {
            stmFindAll.setInt(1, Config.ROW_PER_PAGE);
            stmFindAll.setInt(2, (currentPage - 1) * Config.ROW_PER_PAGE);
            ResultSet resultSet = stmFindAll.executeQuery();
            while (resultSet.next()) {
                UserDTO userDTO = maptoUserDTO(resultSet);
                userDTOS.add(userDTO);
            }
        } catch (SQLException e) {
//            throw new RuntimeException(e);
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return userDTOS;
    }

    @Override
    public int getTotalPage() {
        String getTotalRecordSql = "select count(*) as totalRecord from users";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(getTotalRecordSql)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int totalRecord = resultSet.getInt("totalRecord");
                return (int) (Math.ceil((double) totalRecord / Config.ROW_PER_PAGE));
            }
        } catch (SQLException exception) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return 0;
    }

    // Đổi mật khẩu
    @Override
    public boolean changePassword(int userId, String newPassword) {
        String changePasswordSql = "update users set password = ? where user_id = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement stmChangePassword = connection.prepareStatement(changePasswordSql)) {
            stmChangePassword.setString(1, newPassword);
            stmChangePassword.setInt(2, userId);
            return stmChangePassword.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thủ lại", Config.RESET);
            return false;
        }
    }

    // Xóa người dùng
    @Override
    public boolean deleteUser(int id) {
        String deleteUserSql = "delete from users where user_id = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement stmDeleteUser = connection.prepareStatement(deleteUserSql)) {
            stmDeleteUser.setInt(1, id);
            return stmDeleteUser.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
            return false;
        }
    }

    // Tìm kiếm bằng ID
    @Override
    public UserDTO findById(int id) {
        String findUserSql = "select user_id, full_name, email, role, balance, created_at from users where user_id = ?";
        UserDTO userDTO = null;
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement stmFindUser = connection.prepareStatement(findUserSql)) {
            stmFindUser.setInt(1, id);
            ResultSet resultSet = stmFindUser.executeQuery();
            if (resultSet.next()) {
                userDTO = maptoUserDTO(resultSet);
            }
        } catch (SQLException e) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return userDTO;
    }

    @Override
    public boolean grantRole(int id, String role) {
        String grantRoleSql = "update users set role = ? where user_id = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement stmGrantRole = connection.prepareStatement(grantRoleSql)) {
            stmGrantRole.setString(1, role);
            stmGrantRole.setInt(2, id);
            return stmGrantRole.executeUpdate() > 0;
        } catch (SQLException exception) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return false;
    }
}
