package hehe_com;

import hehe_com.console.InputReader;
import hehe_com.order.OrderController;
import hehe_com.order.OrderRepository;
import hehe_com.order.OrderService;
import hehe_com.product.MenuProductResult;
import hehe_com.product.ProductController;
import hehe_com.product.ProductRepository;
import hehe_com.product.ProductService;
import hehe_com.user.*;

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
            Path ORDER_DIR = Paths.get("data/orders");
            
            InputReader inputReader = new InputReader(scanner);
            
            // user
            UserRepository userRepository = new UserRepository(USER_DIR);
            UserService userService = new UserService(userRepository);
            UserContoller userController = new UserContoller(userService, inputReader, printStream);
            
            // product
            ProductRepository productRepository = new ProductRepository(PRODUCT_DIR);
            ProductService productService = new ProductService(productRepository);
            ProductController productController = new ProductController(productService, inputReader, printStream);
            
            // order
            OrderRepository orderRepository = new OrderRepository(ORDER_DIR);
            OrderService orderService = new OrderService(orderRepository,productRepository,userRepository);
            OrderController orderController = new OrderController(inputReader,orderService,productService,printStream,userService);
            
            boolean running = true;
            while (running) {
                Users userLogin = userController.getUserLogin();
                printStream.println("=== My Application ===");
                printStream.println("1. Profile");
                if(userLogin != null) {
                    printStream.println("2. Product");
                    printStream.println("3. Order");
                }
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
                        if(userLogin == null) {
                            printStream.println("Please Log in");
                            continue;
                        }
                        MenuProductResult res = productController.menu();
                        if (res == MenuProductResult.EXIT) {
                            printStream.println("Bye");
                            return;
                        }
                    }
                    case 3 -> {
                        if(userLogin == null) {
                            printStream.println("Please Log in");
                            continue;

                        }
                        orderController.menu();
                    }
                    case 0 -> {
                        printStream.println("Bye!");
                        running = false;
                    }
                    default -> {
                        if(userLogin == null) {
                            printStream.println("Please Log in");
                        }else{
                            printStream.println("Invalid input");
                        }
                    }
                }
            }
        }
    }
}
