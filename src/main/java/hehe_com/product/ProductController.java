package hehe_com.product;

import hehe_com.utils.InputUtil;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.PrintStream;
import java.util.function.Predicate;

public class ProductController {
    
    private final ProductService productService;
    private final Scanner scanner;
    private final PrintStream print;
    
    public ProductController(ProductService productService, Scanner scanner, PrintStream printStream) {
        this.productService = productService;
        this.scanner = scanner;
        this.print = printStream;
    }
    
    private void printMenu() {
        print.println("=== Menu Products ===");
        print.println("1. Print list of products");
        print.println("2. Add Product");
        print.println("3. Update Product");
        print.println("4. Delete Product");
        print.println("0. Exit");
    }
    
    public MenuProductResult menu() {
        printMenu();
        print.print("==> ");
        switch (Integer.parseInt(scanner.nextLine())) {
            case 1 -> listProducts();
            case 2 -> addProduct();
            case 3 -> updateProduct();
            case 4 -> deleteProduct();
            case 0 -> {
                return MenuProductResult.BACK;
            }
            default -> print.println("Invalid choice.");
        }
        return MenuProductResult.CONTINUE;
    }
    
    public void addProduct() {
        print.print("Enter product name: ");
        String name = InputUtil.getInput(scanner, String.class);
        print.print("Enter product price: ");
        BigDecimal price = InputUtil.getInput(scanner, BigDecimal.class);
        print.print("Enter product stock: ");
        Integer stock = InputUtil.getInput(scanner, Integer.class);
        
        Product newProduct = productService.createProduct(name, price, stock);
        print.println("Product added: " + newProduct);
    }
    
    public void updateProduct() {
        print.print("Enter product ID to update: ");
        long id = scanner.nextLong();
        scanner.nextLine();
        Product product = productService.getProductById(id);
        if (product == null) {
            print.println("Product not found.");
            return;
        }
        
        print.print("Enter new name (" + product.getName() + "): ");
        String name = InputUtil.getInput(scanner, String.class);
        print.print("Enter new price (" + product.getPrice() + "): ");
        BigDecimal price = InputUtil.getInput(scanner, BigDecimal.class);
        print.print("Enter new stock (" + product.getStock() + "): ");
        Integer stock = InputUtil.getInput(scanner, Integer.class);
        
        Product updatedProduct = productService.updateProduct(id, name, price, stock);
        print.println("Product updated: " + updatedProduct);
    }
    
    public void deleteProduct() {
        print.print("Enter product ID to delete: ");
        long id = scanner.nextLong();
        scanner.nextLine();
        
        Product product = productService.getProductById(id);
        
        if (product != null) {
            
            print.println("=[ " + product.getId()+ " | " + product.getName() + " ]=");
            print.print("type 'n' for cancel, press enter to confirm => ");
            
            Character confirm = InputUtil.getInput(scanner, Character.class);
            if (confirm.equals('N') || confirm.equals('n')) return;
            productService.deleteProduct(id);
            print.println("Product deleted: " + product.getName());
        } else {
            print.println("Product not found.");
        }
    }
    
    public void listProducts() {
        Predicate<Product> filter = null;
        PRODUCT_SORT_FIELD filterField = null;
        PRODUCT_SORT_FIELD sortField = null;
        boolean isAsc = true;
        List<Product> products;
        
        while (true) {
            products = productService.getAllProducts(filter, sortField, isAsc);
            print.println("=-- Products : --=");
            products.forEach(print::println);
            
            print.println("[ 1. Filter "+ ((filter == null) ? "All" : filter.getClass().getSimpleName()) +
                    "| 2. " + ((sortField == null) ? "Sort" : "Sorted by " + sortField.getValue()) +
                    " | 3. " + (isAsc ? "Asc" : "Desc") +
                    " | 0. Back ]"
            );
            print.print("==> ");
            
            switch (Integer.parseInt(scanner.nextLine())) {
                case 1 -> filter = this.filterMenuListProducts();
                case 2 -> {
                    print.println("Available sort fields:");
                    print.println("1. by Name | 2. by Price | 3. by Stock");
                    print.print("==> ");
                    sortField = switch (Integer.parseInt(scanner.nextLine())) {
                        case 1 -> PRODUCT_SORT_FIELD.NAME;
                        case 2 -> PRODUCT_SORT_FIELD.PRICE;
                        case 3 -> PRODUCT_SORT_FIELD.STOCK;
                        default -> null;
                    };
                }
                case 3 -> isAsc = !isAsc;
                case 0 -> {
                    return;
                }
                default -> print.println("Invalid choice.");
            }
        }
    }
    
    private Predicate<Product> filterMenuListProducts() {
        print.println("Available filters:");
        print.println("1. By Name | 2. By Price | 3. By Stock");
        print.print("==> ");
        return switch (Integer.parseInt(scanner.nextLine())) {
            case 1 -> {
                print.print("Search by name: ");
                String name = scanner.nextLine();
                yield product -> product.getName().toLowerCase().contains(name.toLowerCase());
            }
            case 2 -> {
                print.println("Filter range price:");
                print.print("Min price: ");
                BigDecimal minPrice = InputUtil.getInput(scanner,BigDecimal.class);
                print.print("Max price (if null, will equal min): ");
                BigDecimal maxPrice = InputUtil.getInput(scanner,BigDecimal.class);
                scanner.nextLine();
                yield product -> (maxPrice == null)
                        ? product.getPrice().compareTo(minPrice) == 0
                        : product.getPrice().compareTo(minPrice) >= 0 && product.getPrice().compareTo(maxPrice) <= 0;
            }
            case 3 -> {
                print.println("Filter range stock:");
                print.print("Min stock: ");
                int minStock = scanner.nextInt();
                print.print("Max stock: ");
                Integer maxStock = scanner.nextInt();
                scanner.nextLine();
                yield product -> (maxStock == null)
                        ? product.getStock() == minStock
                        : product.getStock() >= minStock && product.getStock() <= maxStock;
            }
            default -> null;
        };
    }
    
    private void detailProduct(Product product) {
        print.println("=== Product Details ===");
        print.println("ID: " + product.getId());
        print.println("Name: " + product.getName());
        print.println("Price: " + product.getPrice());
        print.println("Stock: " + product.getStock());
        print.println("Updated at: " + product.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm")));
    }
    
}
