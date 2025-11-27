package hehe_com;

import hehe_com.user.MenuUserResult;
import hehe_com.user.UserContoller;
import hehe_com.user.UserRepository;
import hehe_com.user.UserService;

import java.io.PrintStream;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        try (
                Scanner scanner = new Scanner(System.in);
                PrintStream printStream = new PrintStream(System.out)
        ) {
            UserRepository userRepository = new UserRepository();
            UserService userService = new UserService(userRepository);
            UserContoller userContoller = new UserContoller(userService, scanner, printStream);
            
            boolean running = true;
            while (running) {
                
                printStream.println("=== My Application ===");
                
                printStream.println("1. Profile");
                printStream.println("0. Exit");
                printStream.print("==> ");
                switch (scanner.nextInt()) {
                    case 1 -> {
                        while (true) {
                            MenuUserResult res = userContoller.menu();
                            if(res == MenuUserResult.BACK || res == MenuUserResult.LOGOUT) break;
                        }
                    }
                    
                    case 0 -> {
                        printStream.println("Bye Bye");
                        running = false;
                    }
                    default -> {
                        printStream.println("Invalid input");
                    }
                }
            }
        }
    }
}
