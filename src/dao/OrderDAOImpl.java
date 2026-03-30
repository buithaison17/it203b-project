package dao;

import models.entity.Food;
import utils.Config;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OrderDAOImpl implements OrderDAO {
    private final FoodDAOImpl foodDAO = FoodDAOImpl.getInstance();

    private static OrderDAOImpl instance;

    private OrderDAOImpl() {
    }

    public static OrderDAOImpl getInstance() {
        if (instance == null) {
            instance = new OrderDAOImpl();
        }
        return instance;
    }

    @Override
    public boolean placeOrder(Map<Integer, Integer> orderItems, double totalPrice) {
        Connection connection = new DatabaseConnection().getConnection();
        try {
            connection.setAutoCommit(false);
            // Tạo đơn hàng
            String insertOderSql = "insert into orders(customer_id, total_amount) values (?, ?)";
            PreparedStatement stmInsertOder = connection.prepareStatement(insertOderSql, Statement.RETURN_GENERATED_KEYS);
            stmInsertOder.setInt(1, Config.getUser().getId());
            stmInsertOder.setDouble(2, totalPrice);
            int resultInserOrder = stmInsertOder.executeUpdate();
            ResultSet resultSet = stmInsertOder.getGeneratedKeys();
            resultSet.next();

            // Insert vào order_items và trừ số lượng
            int orderId = resultSet.getInt(1);
            boolean resultInserOrderItem = insertOrderItem(connection, orderId, orderItems);

            // Trừ tiền tài khoản user
            Config.getUser().setBalance(Config.getUser().getBalance() - totalPrice);
            String updateBalanceSql = "update users set balance = balance - ? where user_id = ?";
            PreparedStatement stmUpdateBalance = connection.prepareStatement(updateBalanceSql);
            stmUpdateBalance.setDouble(1, totalPrice);
            stmUpdateBalance.setInt(2, Config.getUser().getId());
            int resultUpdateBalance = stmUpdateBalance.executeUpdate();

            // Commit khi không có lỗi
            connection.commit();
            return resultInserOrder > 0 && resultInserOrderItem && resultUpdateBalance > 0;
        } catch (SQLException exception) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã có lỗi xảy ra vui lòng thử lại", Config.RESET);
            // Rollback khi sảy ra lỗi
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } finally {
            try {
                connection.close();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    private boolean insertOrderItem(Connection connection, int orderId, Map<Integer, Integer> orderItems) throws SQLException {
        // Thêm vào orderItem
        String sqlInserOrderItem = "insert into order_items(order_id, food_id, quantity) values (?, ?, ?)";
        PreparedStatement stmInsertOrderItem = connection.prepareStatement(sqlInserOrderItem);
        for (Map.Entry<Integer, Integer> entry : orderItems.entrySet()) {
            stmInsertOrderItem.setInt(1, orderId);
            stmInsertOrderItem.setInt(2, entry.getKey());
            stmInsertOrderItem.setInt(3, entry.getValue());
            stmInsertOrderItem.addBatch();
        }

        int[] resultInsert = stmInsertOrderItem.executeBatch();
        List<Integer> result1 = Arrays.stream(resultInsert)
                .boxed()
                .toList();

        // Giảm số lượng đơn hàng
        String updateStockSql = "update foods set stock = stock - ? where food_id = ?";
        PreparedStatement stmUpdateStock = connection.prepareStatement(updateStockSql);
        for (Map.Entry<Integer, Integer> entry : orderItems.entrySet()) {
            stmUpdateStock.setInt(1, entry.getValue());
            stmUpdateStock.setInt(2, entry.getKey());
            stmUpdateStock.addBatch();
        }

        int[] resultUpdateStock = stmUpdateStock.executeBatch();
        List<Integer> result2 = Arrays.stream(resultUpdateStock)
                .boxed()
                .toList();

        return result1.stream().allMatch(num -> num == 1) && result2.stream().allMatch(num -> num == 1);
    }
}
