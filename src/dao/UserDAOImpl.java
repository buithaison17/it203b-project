package dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import entity.User;
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
        try (Connection connection = DatabaseConnection.getInstance().getConnection(); PreparedStatement stmSaveUser = connection.prepareStatement(saveUserSql)) {
            stmSaveUser.setString(1, fullName);
            stmSaveUser.setString(2, email);
            stmSaveUser.setString(3, password);
            int result = stmSaveUser.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    // Tìm kiếm bằng email
    @Override
    public User findByEmail(String email) {
        User user = null;
        String findUserSql = "select user_id, full_name, email, password, role, balance, created_at from users where email = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection(); PreparedStatement stmFindUser = connection.prepareStatement(findUserSql)) {
            stmFindUser.setString(1, email);
            ResultSet resultSet = stmFindUser.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String fullName = resultSet.getString("full_name");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                double balance = resultSet.getDouble("balance");
                LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                user = new User(userId, fullName, email, password, role, balance, createdAt);
            }
        } catch (SQLException e) {

        }
        return user;
    }

    private User mapToUser(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getInt("user_id"), resultSet.getString("full_name"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("role"), resultSet.getDouble("balance"), resultSet.getTimestamp("created_at").toLocalDateTime());
    }

    // Lấy danh sách user
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        String findAllSql = "select user_id, full_name, email, password, role, balance, created_at from users order by created_at desc";
        try (Connection connection = DatabaseConnection.getInstance().getConnection(); PreparedStatement stmFindAll = connection.prepareStatement(findAllSql)) {
            ResultSet resultSet = stmFindAll.executeQuery();
            while (resultSet.next()) {
                User user = mapToUser(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return users;
    }
}
