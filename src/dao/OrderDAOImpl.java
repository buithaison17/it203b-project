package dao;

import enums.OrderStatus;
import models.dto.OrderDTO;
import models.entity.Food;
import models.entity.Order;
import utils.Config;
import utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
                connection.setAutoCommit(true);
                connection.close();
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

    @Override
    public int getTotalPage() {
        String getTotalRecordSql = "select count(*) as totalRecord from orders";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(getTotalRecordSql)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int totalRecord = resultSet.getInt("totalRecord");
                return (int) Math.ceil((double) totalRecord / Config.ROW_PER_PAGE);
            }
        } catch (SQLException exception) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return 0;
    }

    private OrderDTO mapToOrderDTO(ResultSet resultSet) throws SQLException {
        int orderId = resultSet.getInt("order_id");
        int customerId = resultSet.getInt("customer_id");
        double totalPrice = resultSet.getDouble("total_amount");
        String status = resultSet.getString("status");
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        return new OrderDTO(orderId, customerId, totalPrice, OrderStatus.valueOf(status.toUpperCase()), createdAt);
    }

    @Override
    public List<OrderDTO> viewListOrder(int currentPage) {
        List<OrderDTO> orderDTOS = new ArrayList<>();
        String sql = "select order_id, customer_id, total_amount, status, created_at from orders limit ? offset ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement stmViewListOrder = connection.prepareStatement(sql)) {
            stmViewListOrder.setInt(1, Config.ROW_PER_PAGE);
            stmViewListOrder.setInt(2, (currentPage - 1) * Config.ROW_PER_PAGE);
            ResultSet resultSet = stmViewListOrder.executeQuery();
            while (resultSet.next()) {
                OrderDTO orderDTO = mapToOrderDTO(resultSet);
                orderDTOS.add(orderDTO);
            }
        } catch (SQLException e) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return orderDTOS;
    }

    @Override
    public boolean updateStatusOrder(int orderId, String status) {
        String sql = "update orders set status = ? where order_id = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, orderId);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return false;
    }

    @Override
    public Order findById(int id) {
        String findByIdSql = "select order_id, customer_id, total_amount, status, created_at from orders where order_id = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(findByIdSql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapToOrder(resultSet);
            }
        } catch (SQLException exception) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return null;
    }

    private Order mapToOrder(ResultSet resultSet) throws SQLException {
        int orderId = resultSet.getInt("order_id");
        int customerId = resultSet.getInt("customer_id");
        double totalPrice = resultSet.getDouble("total_amount");
        String status = resultSet.getString("status");
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        return new Order(orderId, customerId, OrderStatus.valueOf(status.toUpperCase()), totalPrice, createdAt);
    }
}
