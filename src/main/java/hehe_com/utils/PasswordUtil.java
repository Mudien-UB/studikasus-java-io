package hehe_com.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil {
    
    // Hash password
    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
    
    // Cek password
    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified;
    }
    
}
