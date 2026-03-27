package utils;

import exceptions.InvalidEmailException;
import exceptions.InvalidRoleException;

public class Validate {
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
}
