package hehe_com.utils;

import java.math.BigDecimal;
import java.util.Scanner;

public class InputUtil {
    
    
    public static <T> T getInput(Scanner scanner, Class<T> type) {
        String input = scanner.nextLine();
        
        if (input.isEmpty()) return null;
        
        try {
            
            if (type == String.class) return type.cast(input);
            if (type == Character.class) return type.cast(input.charAt(0));
            if (type == Integer.class) return type.cast(Integer.parseInt(input));
            if (type == Long.class) return type.cast(Long.parseLong(input));
            if (type == Double.class) return type.cast(Double.parseDouble(input));
            if (type == BigDecimal.class) return type.cast(new BigDecimal(input));
            
            throw new IllegalArgumentException("Unsupported type: " + type);
            
        } catch (Exception e) {
            return null;
        }
    }
    
    public static char getRequiredChar(Scanner scanner) {
        String input = scanner.nextLine();
        if("".equals(input)) return '\0';
        return input.charAt(0);
    }
    
}
