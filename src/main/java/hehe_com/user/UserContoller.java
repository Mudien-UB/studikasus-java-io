package hehe_com.user;

import java.io.PrintStream;
import java.util.Scanner;

public class UserContoller {
    
    private final UserService userService;
    private final Scanner scanner;
    private final PrintStream print;
    private Users userLogin;
    
    public UserContoller(UserService userService, Scanner scanner, PrintStream print) {
        this.userService = userService;
        this.scanner = scanner;
        this.print = print;
        userLogin = userService.getUserLogin();
    }
    
    public MenuUserResult menu() {
        
        if (userLogin == null) {
            print.println("=== Welcome ===");
            print.println("1. Register");
            print.println("2. Login");
            print.println("0. Back");
            print.print("==> ");
            
            switch (scanner.nextInt()) {
                case 1 -> this.register();
                case 2 -> this.login();
                case 0 -> {return MenuUserResult.BACK;}
                default ->print.println("Invalid input");
            }
            
        } else {
            print.println("=== Hi, " + userLogin.getUsername() + " ! ===");
            print.println("1. Show Profile");
            print.println("2. Logout");
            print.println("0. Back");
            print.print("==> ");
            
            switch (scanner.nextInt()) {
                case 1 -> this.showProfile();
                case 2 -> {
                    this.logout();
                    return MenuUserResult.LOGOUT;
                }
                case 0 -> {return MenuUserResult.BACK;}
                default ->print.println("Invalid input");
            }
        }
        return MenuUserResult.CONTINUE;
    }
    
    public Users getUserLogin() {
        return this.userLogin;
    }
    
    public void login() {
        
        if (userLogin != null) return;
        
        print.println(" === Login === ");
        print.print(" Username: ");
        String username = scanner.next();
        print.print(" Password: ");
        String password = scanner.next();
        
        userLogin = userService.Login(username.toLowerCase(), password);
    }
    
    public void register() {
        if (userLogin != null) return;
        print.println(" === Register ===");
        print.print(" Username: ");
        String username = scanner.next();
        print.print(" Email: ");
        String email = scanner.next();
        print.print(" Password: ");
        String password = scanner.next();
        
        userLogin = userService.createUser(username.toLowerCase(), email.toLowerCase(), password);
    }
    
    public void logout() {
        if (userLogin == null) return;
        userService.logout();
        userLogin = null;
        print.println(" === Logout ===");
    }
    
    public void showProfile() {
        if (userLogin == null) return;
        print.println("+ === Profile ===");
        print.println("| Username: " + userLogin.getUsername());
        print.println("| Email: " + userLogin.getEmail());
        print.println("| Role: " + userLogin.getRole());
        print.println("+ ---------------");
        print.println("");
    }
    
}
