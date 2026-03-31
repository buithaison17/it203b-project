package dao;

import enums.BookingStatus;
import models.dto.BookingDTO;
import models.dto.ComputerDTO;
import models.dto.ComputerStatistics;
import models.entity.Category;
import utils.Config;
import utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {
    private final UserDAOImpl userDAO = UserDAOImpl.getInstance();
    private final ComputerDAOImpl computerDAO = ComputerDAOImpl.getInstance();
    private static BookingDAOImpl instance;

    private BookingDAOImpl() {
    }

    public static BookingDAOImpl getInstance() {
        if (instance == null) {
            instance = new BookingDAOImpl();
        }
        return instance;
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

        try (Connection connection = new DatabaseConnection().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
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
    public boolean bookingComputer(int computerId, LocalDateTime startTime, LocalDateTime endTime, double totalPrice) {
        Connection connection = new DatabaseConnection().getConnection();
        try {
            connection.setAutoCommit(false);
            // Thêm vào booking
            String sqlBookingComputer = "insert into booking(customer_id, computer_id, start_time, end_time, total_amount) values (?, ?, ?, ?, ?)";
            PreparedStatement stmBookingComputer = connection.prepareStatement(sqlBookingComputer);
            stmBookingComputer.setInt(1, Config.getUser().getId());
            stmBookingComputer.setInt(2, computerId);
            stmBookingComputer.setTimestamp(3, Timestamp.valueOf(startTime));
            stmBookingComputer.setTimestamp(4, Timestamp.valueOf(endTime));
            stmBookingComputer.setDouble(5, totalPrice);
            int resultBookingComputer = stmBookingComputer.executeUpdate();

            // Trừ tiền người dùng
            Config.getUser().setBalance(Config.getUser().getBalance() - totalPrice);
            String sqlUpdateBalance = "update users set balance = balance - ? where user_id = ?";
            PreparedStatement stmUpdateBalance = connection.prepareStatement(sqlUpdateBalance);
            stmUpdateBalance.setDouble(1, totalPrice);
            stmUpdateBalance.setInt(2, Config.getUser().getId());
            int resultUpdateBalance = stmUpdateBalance.executeUpdate();

            connection.commit();
            return resultBookingComputer > 0 && resultUpdateBalance > 0;
        } catch (SQLException exception) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        } finally {
            try {
                connection.setAutoCommit(false);
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    private BookingDTO mapToBookingDTO(ResultSet resultSet) throws SQLException {
        int bookingId = resultSet.getInt("booking_id");
        int customerId = resultSet.getInt("customer_id");
        String customerName = userDAO.findById(customerId).getFullName();
        int computerId = resultSet.getInt("computer_id");
        String computerName = computerDAO.findComputerById(computerId).getName();
        LocalDateTime startTime = resultSet.getTimestamp("start_time").toLocalDateTime();
        LocalDateTime endTime = resultSet.getTimestamp("end_time").toLocalDateTime();
        double totalAmount = resultSet.getDouble("total_amount");
        String statusString = resultSet.getString("status");
        BookingStatus status = BookingStatus.valueOf(statusString.toUpperCase());
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        return new BookingDTO(bookingId, customerName, computerName, startTime, endTime, totalAmount, status, createdAt);
    }

    @Override
    public List<BookingDTO> viewListBooking(int currentPage) {
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        String sql = "select booking_id, customer_id, computer_id, start_time, end_time, total_amount, status, created_at from booking limit ? offset ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement stmViewListBooking = connection.prepareStatement(sql)) {
            stmViewListBooking.setInt(1, Config.ROW_PER_PAGE);
            stmViewListBooking.setInt(2, (currentPage - 1) * Config.ROW_PER_PAGE);
            ResultSet resultSet = stmViewListBooking.executeQuery();
            while (resultSet.next()) {
                BookingDTO bookingDTO = mapToBookingDTO(resultSet);
                bookingDTOS.add(bookingDTO);
            }
        } catch (SQLException e) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return bookingDTOS;
    }

    @Override
    public int getTotalPage() {
        String getTotalRecordSql = "select count(*) as totalRecord from booking";
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

    @Override
    public List<BookingDTO> viewListBookingOfUser(int currentPage) {
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        String sql = "select booking_id, customer_id, computer_id, start_time, end_time, total_amount, status, created_at from booking where customer_id = ? limit ? offset ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement stmViewListBooking = connection.prepareStatement(sql)) {
            stmViewListBooking.setInt(1, Config.getUser().getId());
            stmViewListBooking.setInt(2, Config.ROW_PER_PAGE);
            stmViewListBooking.setInt(3, (currentPage - 1) * Config.ROW_PER_PAGE);
            ResultSet resultSet = stmViewListBooking.executeQuery();
            while (resultSet.next()) {
                BookingDTO bookingDTO = mapToBookingDTO(resultSet);
                bookingDTOS.add(bookingDTO);
            }
        } catch (SQLException e) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return bookingDTOS;
    }

    @Override
    public int getTotalPageOfUser() {
        String getTotalRecordSql = "select count(*) as totalRecord from booking where customer_id = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(getTotalRecordSql)) {
            statement.setInt(1, Config.getUser().getId());
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

    @Override
    public int getTotalPageListBestComputer() {
        String getTotalRecordSql = "select count(*) as totalRecord from booking";
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

    private ComputerStatistics mapToComputerStatistics(ResultSet resultSet) throws SQLException {
        int computerId = resultSet.getInt("computer_id");
        String name = resultSet.getString("name");
        int totalBooking = resultSet.getInt("total_booking");
        double totalAmount = resultSet.getDouble("total_revenue");
        return new ComputerStatistics(computerId, name, totalBooking, totalAmount);
    }

    @Override
    public List<ComputerStatistics> getListBestComputer(int currentPage) {
        List<ComputerStatistics> computerStatisticsList = new ArrayList<>();
        String sql = "select c.computer_id, c.name, count(b.computer_id) as total_booking, sum(b.total_amount) as total_revenue " +
                "from computers c " +
                "join booking b on c.computer_id = b.computer_id " +
                "group by c.computer_id " +
                "limit ? offset ?;";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Config.ROW_PER_PAGE);
            statement.setInt(2, (currentPage - 1) * Config.ROW_PER_PAGE);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ComputerStatistics computerStatistics = mapToComputerStatistics(resultSet);
                computerStatisticsList.add(computerStatistics);
            }
        } catch (SQLException exception) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return computerStatisticsList;
    }

    @Override
    public double getRevenue() {
        String sql = "select sum(total_amount) as totalAmount from booking";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("totalAmount");
            }
        } catch (SQLException exception) {
            System.out.printf("%s%s%s\n", Config.RED, "Đã xảy ra lỗi vui lòng thử lại", Config.RESET);
        }
        return 0.0;
    }
}
