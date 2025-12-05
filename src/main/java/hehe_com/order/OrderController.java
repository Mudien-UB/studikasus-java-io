package hehe_com.order;

import hehe_com.console.InputReader;
import hehe_com.product.Product;
import hehe_com.product.ProductService;
import hehe_com.user.UserService;
import hehe_com.user.Users;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class OrderController {
    
    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;
    private final InputReader inputReader;
    private final PrintStream print;
    
    private final List<OrderDetail> cart = new ArrayList<>();
    
    public OrderController(InputReader inputReader, OrderService orderService,
                           ProductService productService, PrintStream print,
                           UserService userService) {
        this.inputReader = inputReader;
        this.orderService = orderService;
        this.productService = productService;
        this.print = print;
        this.userService = userService;
    }
    
    public void menu() {
        
        while (true) {
            
            print.println("\n=== Create Order ===");
            
            if (cart.isEmpty()) {
                print.println("Cart is empty.");
            } else {
                for (int i = 0; i < cart.size(); i++) {
                    print.println((i + 1) + ". " + cart.get(i));
                }
            }
            
            print.println(
                    "[A] Add product" +
                    "[R] Remove product " +
                    "[S] Submit order " +
                    "[C] Clear cart " +
                    "\n" +
                    "[H] History Orders " +
                    "[Q] Back "
            );
            print.print("==> ");
            char c = inputReader.getInput(Character.class, '\0');
            
            switch (c) {
                case 'A','a' -> addProduct();
                case 'R','r' -> removeProduct();
                case 'S','s' -> submitOrder();
                case 'C','c' -> cart.clear();
                case 'H','h' -> showHistoryOrder();
                case 'Q','q' -> { return; }
                default -> print.println("Invalid option");
            }
        }
    }
    
    private void addProduct() {
        
        print.print("Product ID: ");
        Long id = inputReader.getInput(Long.class, print);
        
        Product product = productService.getProductById(id);
        if (product == null) {
            print.println("Product not found.");
            return;
        }
        
        print.print("Quantity: ");
        int amount = inputReader.getInput(Integer.class, print);
        
        for (OrderDetail d : cart) {
            if (d.getProduct().equals(product)) {
                d.addAmount(amount);
                print.println("Updated quantity.");
                return;
            }
        }
        
        cart.add(new OrderDetail(product, amount));
        print.println("Added.");
    }
    
    private void removeProduct() {
        
        if (cart.isEmpty()) {
            print.println("Cart empty.");
            return;
        }
        
        print.print("Number: ");
        int idx = inputReader.getInput(Integer.class, print) - 1;
        
        if (idx < 0 || idx >= cart.size()) {
            print.println("Invalid index.");
            return;
        }
        
        cart.remove(idx);
        print.println("Removed.");
    }
    
    public void showHistoryOrder(){
        
        Users user = userService.getUserLogin();
        List<Order> listOrders = orderService.LoadAllOrders(user);
        print.println("\n");
        print.println(" >>> Order History <<<");
        if (listOrders.isEmpty()) {
            print.println("No orders found.");
        }else {
            listOrders.forEach(print::println);
        }
    }
    
    private void submitOrder() {
        
        if (cart.isEmpty()) {
            print.println("Cart is empty.");
            return;
        }
        
        Users user = userService.getUserLogin();
        Order order = orderService.submitOrder(user, cart);
        
        cart.clear();
        print.println("Order submitted.");
    }
}
