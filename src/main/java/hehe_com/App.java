package hehe_com;

import hehe_com.product.MenuProductResult;
import hehe_com.product.ProductController;
import hehe_com.product.ProductRepository;
import hehe_com.product.ProductService;
import hehe_com.user.MenuUserResult;
import hehe_com.user.UserContoller;
import hehe_com.user.UserRepository;
import hehe_com.user.UserService;

import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        try (
                Scanner scanner = new Scanner(System.in);
                PrintStream printStream = new PrintStream(System.out)
        ) {
            Path USER_DIR = Paths.get("data/users");
            Path PRODUCT_DIR = Paths.get("data/products");
            
            // user
            UserRepository userRepository = new UserRepository(USER_DIR);
            UserService userService = new UserService(userRepository);
            UserContoller userController = new UserContoller(userService, scanner, printStream);
            
            // product
            ProductRepository productRepository = new ProductRepository(PRODUCT_DIR);
            ProductService productService = new ProductService(productRepository);
            ProductController productController = new ProductController(productService, scanner, printStream);
            
            boolean running = true;
            while (running) {
                printStream.println("=== My Application ===");
                printStream.println("1. Profile");
                printStream.println("2. Product");
                printStream.println("0. Exit");
                printStream.print("==> ");
                
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1 -> {
                        while (true) {
                            MenuUserResult res = userController.menu();
                            if (res != MenuUserResult.CONTINUE) break;
                        }
                    }
                    case 2 -> {
                        while (true) {
                            MenuProductResult res = productController.menu();
                            if (res != MenuProductResult.CONTINUE) break;
                        }
                    }
                    case 0 -> {
                        printStream.println("Bye Bye");
                        running = false;
                    }
                    default -> printStream.println("Invalid input");
                }
            }
        }
    }
}
