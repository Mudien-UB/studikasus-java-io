package hehe_com.product;

import hehe_com.utils.InputUtil;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
        Predicate<Product> predicate = null;
        PRODUCT_SORT_FIELD sortField = null;
        boolean isAsc = true;
        List<Product> products;
        
        while (true) {
            products = productService.getAllProducts(predicate, sortField, isAsc);
            products.forEach(print::println);
            print.println(
                    "[A] Add Product" +
                            " | [U] Update Product" +
                            " | [D] Delete Product" +
                            " | [F] Filter " + ((predicate != null) ? " [R] Reset " : "") +
                            " | [S] Sort" +
                            " | [T] " + (isAsc ? "Ascending" : "Descending") +
                            " | [Q] Back" +
                            " | [X] Exit"
            );
            print.print("==> ");
            
            switch (InputUtil.getRequiredChar(scanner)) {
                case 'A', 'a' -> addProduct();
                case 'U', 'u' -> updateProduct();
                case 'D', 'd' -> deleteProduct();
                case 'F', 'f' -> predicate = this.filterMenuListProducts();
                case 'R','r' -> predicate = null;
                case 'S', 's' -> {
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
                case 'T','t' -> isAsc = !isAsc;
                case 'Q', 'q' -> {
                    return MenuProductResult.BACK;
                }
                case 'X','x' -> {
                    return MenuProductResult.EXIT;
                }
                default -> print.println("Invalid choice.");
            }
        }
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
            print.println("=[ " + product.getId() + " | " + product.getName() + " ]=");
            print.print("type 'n' for cancel, press enter to confirm => ");
            
            Character confirm = InputUtil.getRequiredChar(scanner);
            if (confirm.equals('N') || confirm.equals('n')) return;
            productService.deleteProduct(id);
            print.println("Product deleted: " + product.getName());
        } else {
            print.println("Product not found.");
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
                BigDecimal minPrice = InputUtil.getInput(scanner, BigDecimal.class);
                print.print("Max price (if null, will equal min): ");
                BigDecimal maxPrice = InputUtil.getInput(scanner, BigDecimal.class);
                
                yield product -> (maxPrice == null)
                        ? product.getPrice().compareTo(minPrice) == 0
                        : product.getPrice().compareTo(minPrice) >= 0 && product.getPrice().compareTo(maxPrice) <= 0;
            }
            case 3 -> {
                print.println("Filter range stock:");
                print.print("Min stock: ");
                int minStock = scanner.nextInt();
                print.print("Max stock (if null, will equal min): ");
                Integer maxStock = InputUtil.getInput(scanner, Integer.class);
                
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
