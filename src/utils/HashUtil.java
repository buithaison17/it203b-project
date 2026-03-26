package utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class HashUtil {
    public static String hashPassword(String password) {
        BCrypt.Hasher hasher = BCrypt.withDefaults();
        String hashPassword = hasher.hashToString(12, password.toCharArray());
        return hashPassword;
    }
}
