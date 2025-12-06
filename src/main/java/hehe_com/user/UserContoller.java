package hehe_com.user;

import hehe_com.console.InputReader;

import java.io.PrintStream;

public class UserContoller {
    
    private final UserService userService;
    private final InputReader inputReader;
    private final PrintStream print;
    
    public UserContoller(UserService userService, InputReader inputReader, PrintStream print) {
        this.userService = userService;
        this.inputReader = inputReader;
        this.print = print;
    }
    
    public MenuUserResult menu() {
        Users userLogin = userService.getUserLogin();
        if (userLogin == null) {
            print.println("=== Welcome ===");
            print.println("1. Register");
            print.println("2. Login");
            print.println("0. Back");
            print.print("==> ");
            
            switch (inputReader.getInput(Integer.class, -1)) {
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
            
            switch (inputReader.getInput(Integer.class, -1)) {
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
        return userService.getUserLogin();
    }
    
    public void login() {
        Users userLogin = userService.getUserLogin();
        if (userLogin != null) return;
        
        print.println(" === Login === ");
        print.print(" Username: ");
        String username = inputReader.getInput(String.class);
        print.print(" Password: ");
        String password = inputReader.getInput(String.class);
        
        userLogin = userService.Login(username.toLowerCase(), password);
    }
    
    public void register() {
        Users userLogin = userService.getUserLogin();
        if (userLogin != null) return;
        print.println(" === Register ===");
        print.print(" Username: ");
        String username = inputReader.getInput(String.class, print);
        print.print(" Email: ");
        String email = inputReader.getInput(String.class, print);
        print.print(" Password: ");
        String password = inputReader.getInput(String.class, print);
        
        userLogin = userService.createUser(username.toLowerCase(), email.toLowerCase(), password);
    }
    
    public void logout() {

        Users userLogin = userService.getUserLogin();
        if (userLogin == null) return;
        userService.logout();
        print.println(" === Logout ===");
    }
    
    public void showProfile() {
        Users userLogin = userService.getUserLogin();
        if (userLogin == null) return;
        print.println("+ === Profile ===");
        print.println("| Username: " + userLogin.getUsername());
        print.println("| Email: " + userLogin.getEmail());
        print.println("| Role: " + userLogin.getRole());
        print.println("+ ---------------");
        print.println("");
    }
    
}
