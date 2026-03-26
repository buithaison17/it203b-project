package utils;

import exceptions.InvalidEmailException;

public class Validate {
    public static void validateEmailFormat(String email) throws InvalidEmailException {
        if (!email.matches("^[a-zA-Z0-9_+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidEmailException("Email không đúng định dạng");
        }
    }
}
