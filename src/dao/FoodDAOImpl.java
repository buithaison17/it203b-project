package dao;

import models.entity.Food;
import utils.Config;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FoodDAOImpl implements FoodDAO {
    private static FoodDAOImpl instance = null;

    private FoodDAOImpl() {
    }

    public static FoodDAOImpl getInstance() {
        if (instance == null) {
            instance = new FoodDAOImpl();
        }
        return instance;
    }

    @Override
    public boolean saveFood(String name, String description, double price, int stock) {
        String saveFoodSql = "insert into foods(name, description, price, stock) values (?,?,?,?)";
        try (Connection connection = new DatabaseConnection().getConnection(); PreparedStatement statement = connection.prepareStatement(saveFoodSql)) {
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setDouble(3, price);
            statement.setInt(4, stock);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return false;
    }

    @Override
    public boolean deleteFood(int foodId) {
        String deleteFoodSql = "delete from foods where food_id = ?";
        try (Connection connection = new DatabaseConnection().getConnection(); PreparedStatement statement = connection.prepareStatement(deleteFoodSql)) {
            statement.setInt(1, foodId);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return false;
    }

    @Override
    public boolean updateFood(int foodId, String name, String description, double price) {
        String updateFoodSql = "update foods set name = ?, description = ?, price = ? where food_id = ?";
        try (Connection connection = new DatabaseConnection().getConnection(); PreparedStatement statement = connection.prepareStatement(updateFoodSql)) {
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setDouble(3, price);
            statement.setInt(4, foodId);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return false;
    }

    @Override
    public boolean updateStock(int foodId, int newStock) {
        String updateStockSql = "update foods set stock = ? where food_id = ?";
        try (Connection connection = new DatabaseConnection().getConnection(); PreparedStatement statement = connection.prepareStatement(updateStockSql)) {
            statement.setInt(1, newStock);
            statement.setInt(2, foodId);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return false;
    }

    private Food mapToFood(ResultSet resultSet) throws SQLException {
        return new Food(
                resultSet.getInt("food_id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDouble("price"),
                resultSet.getInt("stock"),
                resultSet.getTimestamp("created_at").toLocalDateTime());
    }

    @Override
    public Food findById(int foodId) {
        String findByIdSql = "select food_id, name, description, price, stock, created_at from foods where food_id = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(findByIdSql)) {
            statement.setInt(1, foodId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapToFood(resultSet);
            }
        } catch (SQLException exception) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return null;
    }

    @Override
    public List<Food> findAll(int currentPage) {
        List<Food> foods = new ArrayList<>();
        String findAllFoodsSql = "select food_id, name, description, price, stock, created_at from foods limit ? offset ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(findAllFoodsSql)) {
            statement.setInt(1, Config.ROW_PER_PAGE);
            statement.setInt(2, (currentPage - 1) * Config.ROW_PER_PAGE);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                foods.add(mapToFood(resultSet));
            }
        } catch (SQLException exception) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return foods;
    }

    @Override
    public int getTotalPage() {
        String getTotalRecordSql = "select count(*) as totalRecord from foods";
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
}
