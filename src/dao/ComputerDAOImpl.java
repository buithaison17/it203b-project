package dao;

import enums.ComputerStatus;
import models.dto.ComputerDTO;
import models.entity.Category;
import models.entity.Computer;
import utils.Config;
import utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ComputerDAOImpl implements ComputerDAO {

    private static ComputerDAOImpl instance;

    private ComputerDAOImpl() {
    }

    public static ComputerDAOImpl getInstance() {
        if (instance == null) {
            instance = new ComputerDAOImpl();
        }
        return instance;
    }

    @Override
    public boolean saveComputer(String name, String configuration, int categoryId, double price, String status) {
        String saveComputerSql = "insert into computers(name, configuration, price, status, category_id) values (?, ?, ?, ?, ?)";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement stmSaveComputer = connection.prepareStatement(saveComputerSql)) {
            stmSaveComputer.setString(1, name);
            stmSaveComputer.setString(2, configuration);
            stmSaveComputer.setDouble(3, price);
            stmSaveComputer.setString(4, status);
            stmSaveComputer.setInt(5, categoryId);
            return stmSaveComputer.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.printf("%s%s%s", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return false;
    }

    @Override
    public Computer findComputerById(int id) {
        String findComputerByIdSql = "select computer_id, name, configuration, price, status, category_id, created_at from computers where computer_id = ?";
        Computer computer = null;
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement stmFindComputerById = connection.prepareStatement(findComputerByIdSql)) {
            stmFindComputerById.setInt(1, id);
            ResultSet resultSet = stmFindComputerById.executeQuery();
            if (resultSet.next()) {
                computer = mapToComputer(resultSet);
            }
        } catch (SQLException e) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return computer;
    }

    @Override
    public boolean updateComputer(int id, String name, String configuration, int categoryId, double price, String status) {
        String updateComputerSql = "update computers set name = ?, configuration = ?, price = ?, status = ?, category_id = ? where computer_id = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(updateComputerSql)) {
            statement.setString(1, name);
            statement.setString(2, configuration);
            statement.setDouble(3, price);
            statement.setString(4, status);
            statement.setInt(5, categoryId);
            statement.setInt(6, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return false;
    }

    @Override
    public boolean deleteComputer(int id) {
        String deleteComputerSql = "delete from computers where computer_id = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteComputerSql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return false;
    }

    private Computer mapToComputer(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("computer_id");
        String name = resultSet.getString("name");
        String configuration = resultSet.getString("configuration");
        double price = resultSet.getDouble("price");
        String statusString = resultSet.getString("status");
        int categoryId = resultSet.getInt("category_id");
        ComputerStatus status = ComputerStatus.valueOf(statusString.toUpperCase());
        Category category = CategoryDaoImpl.getInstance().findById(categoryId);
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        return new Computer(id, name, configuration, category, price, status, createdAt);
    }

    @Override
    public List<Computer> findAll(int currentPage) {
        List<Computer> computers = new ArrayList<>();
        String findAllComputersSql = "select computer_id, name, configuration, price, status, category_id, created_at from computers limit ? offset ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement stmFindAllComputers = connection.prepareStatement(findAllComputersSql)) {
            stmFindAllComputers.setInt(1, Config.ROW_PER_PAGE);
            stmFindAllComputers.setInt(2, (currentPage - 1) * Config.ROW_PER_PAGE);
            ResultSet resultSet = stmFindAllComputers.executeQuery();
            while (resultSet.next()) {
                Computer computer = mapToComputer(resultSet);
                computers.add(computer);
            }
        } catch (SQLException e) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return computers;
    }

    @Override
    public int getTotalPage() {
        String getTotalRecordSql = "select count(*) as totalRecord from computers";
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

    private ComputerDTO mapToComputerDTO(ResultSet resultSet) throws SQLException {
        int computerId = resultSet.getInt("computer_id");
        String name = resultSet.getString("name");
        String configuration = resultSet.getString("configuration");
        double price = resultSet.getDouble("price");
        int categoryId = resultSet.getInt("category_id");
        Category category = CategoryDaoImpl.getInstance().findById(categoryId);
        return new ComputerDTO(computerId, name, configuration, price, category);
    }

    @Override
    public List<ComputerDTO> getListComputerCanBook(int currentPage, LocalDateTime startTime, LocalDateTime endTime) {
        List<ComputerDTO> computers = new ArrayList<>();

        String sql = """
                 select c.computer_id, c.name, c.configuration, c.price, c.category_id from computers c
                left join booking b on c.computer_id = b.computer_id
                and b.status in ('CONFIRMED', 'IN_PROGRESS') and not (b.start_time > ? or b.end_time < ?)
                where c.status = 'AVAILABLE' and b.booking_id is null
                """;

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setTimestamp(1, Timestamp.valueOf(startTime));
            statement.setTimestamp(2, Timestamp.valueOf(endTime));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ComputerDTO computerDTO = mapToComputerDTO(resultSet);
                computers.add(computerDTO);
            }
        } catch (SQLException e) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return computers;
    }
}
