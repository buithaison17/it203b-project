package utils;

import dao.CategoryDaoImpl;
import exceptions.InvalidCategoryException;
import exceptions.InvalidComputerStatusException;
import exceptions.InvalidEmailException;
import exceptions.InvalidRoleException;

public class Validate {
    private static CategoryDaoImpl categoryDao = CategoryDaoImpl.getInstance();

    public static void validateEmailFormat(String email) throws InvalidEmailException {
        if (!email.matches("^[a-zA-Z0-9_+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidEmailException("Email không đúng định dạng");
        }
    }

    public static void validateRole(String role) throws InvalidRoleException {
        if (!role.toLowerCase().matches("^admin|staff|customer$")) {
            throw new InvalidEmailException("Vai trò không đúng định dạng");
        }
    }

    public static void validateComputerStats(String status) throws InvalidComputerStatusException {
        if (!status.toLowerCase().matches("^available|unavailable$")) {
            throw new InvalidComputerStatusException("Trạng thái máy không đúng định dạng");
        }
    }

    public static void validateCategory(int categoryId) throws InvalidCategoryException {
        if (categoryDao.findById(categoryId) == null) {
            throw new InvalidCategoryException("Khu vực không tồn tại");
        }
    }
}
