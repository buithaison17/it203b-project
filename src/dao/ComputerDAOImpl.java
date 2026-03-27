package dao;

import enums.ComputerStatus;
import models.entity.Computer;
import utils.Config;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ComputerDAOImpl implements ComputerDAO {
    @Override
    public boolean saveComputer(String name, String configuration, int categoryId, double price, ComputerStatus statusdAt) {
        String saveComputerSql = "insert into computers(name, configuration, price, status, category_id) values (?, ?, ?, ?, ?)";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement stmSaveComputer = connection.prepareStatement(saveComputerSql)) {
            stmSaveComputer.setString(1, name);
            stmSaveComputer.setString(2, configuration);
            stmSaveComputer.setDouble(3, price);
            stmSaveComputer.setString(4, statusdAt.toString());
            stmSaveComputer.setInt(5, categoryId);
            return stmSaveComputer.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.printf("%s%s%s", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
            return false;
        }
    }

    @Override
    public Computer findComputerById(int id) {
        return null;
    }

    @Override
    public boolean updateComputer(int id, String name, String configuration, int categoryId, double price, ComputerStatus status) {
        return false;
    }

    @Override
    public boolean deleteComputer(int id) {
        return false;
    }

    @Override
    public List<Computer> findAll() {
        return List.of();
    }
}
