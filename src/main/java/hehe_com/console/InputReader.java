package hehe_com.console;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Scanner;

public class InputReader {
    
    private final Scanner scanner;
    
    public InputReader(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public Scanner getScanner() {
        return this.scanner;
    }
    
    public <T> T getInput( Class<T> type) {
        
        String input = this.scanner.nextLine();
        
        if (input.isBlank()) return null;
        
        try {
            return parse(input, type);
        } catch (Exception e) {
            return null;
        }
    }
    
    public <T> T getInput( Class<T> type, T defaultValue) {
        String input = this.scanner.nextLine();
        
        if (input.isBlank()) return defaultValue;
        
        try {
            return parse(input, type);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    
    public <T> T getInput(Class<T> type, PrintStream printStream) {
        
        while (true) {
            String input = waitForInput(printStream);
            try {
                return parse(input, type);
            } catch (Exception e) {
                printStream.println("Please input " + type.getSimpleName().toLowerCase() + ".");
                printStream.print("?> ");
            }
        }
    }
    
    private <T> T parse(String input, Class<T> type) throws Exception {
        
        if (type == String.class) return type.cast(input);
        if (type == Character.class) return type.cast(input.charAt(0));
        if (type == Integer.class) return type.cast(Integer.parseInt(input));
        if (type == Long.class) return type.cast(Long.parseLong(input));
        if (type == Double.class) return type.cast(Double.parseDouble(input));
        if (type == BigDecimal.class) return type.cast(new BigDecimal(input));
        
        throw new IllegalArgumentException("Unsupported type: " + type);
    }
    
    private String waitForInput( PrintStream printStream) {
        while (true) {
            String input = this.scanner.nextLine();
            if (!input.isBlank()) return input;
            printStream.println("Cannot be empty.");
            printStream.print("?> ");
        }
    }
    
    
}
