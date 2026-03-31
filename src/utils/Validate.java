package utils;

import dao.CategoryDaoImpl;

import java.time.LocalDateTime;

public class Validate {
    private static CategoryDaoImpl categoryDao = CategoryDaoImpl.getInstance();

    public static boolean validateEmailFormat(String email) {
        return email.matches("^[a-zA-Z0-9_+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean validateRole(String role) {
        return role.toLowerCase().matches("^admin|staff|customer$");
    }

    public static boolean validateComputerStats(String status) {
        return status.toLowerCase().matches("^available|unavailable$");
    }

    public static boolean validateCategory(int categoryId) {
        return categoryDao.findById(categoryId) == null;
    }

    public static boolean validateDateTimeMoreThanNow(LocalDateTime startTime) {
        return startTime.isAfter(LocalDateTime.now());
    }

    public static boolean validateStartTimeLessThanEndTime(LocalDateTime startTime, LocalDateTime endTime) {
        return startTime.isBefore(endTime);
    }


    public static boolean validateOrderStatus(String status) {
        return status.toLowerCase().matches("^pending|preparing|served|cancelled$");
    }
}