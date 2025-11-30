package hehe_com.product;


import hehe_com.console.InputReader;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.io.PrintStream;
import java.util.function.Predicate;

public class ProductController {
    
    private final ProductService productService;
    private final InputReader inputReader;
    private final PrintStream print;
    
    public ProductController(ProductService productService, InputReader inputReader, PrintStream printStream) {
        this.productService = productService;
        this.inputReader = inputReader;
        this.print = printStream;
    }
    
    public MenuProductResult menu() {
        Predicate<Product> predicate = null;
        PRODUCT_SORT_FIELD sortField = null;
        boolean isAsc = true;
        List<Product> products;
        
        while (true) {
            print.println();
            products = productService.getAllProducts(predicate, sortField, isAsc);
            
            if(products.isEmpty()) print.println("Nothing to show.");
            else products.forEach(print::println);
            
            print.println();
            print.println("=== Select an option: ===");
            print.println(
                    " [V] view Detail" +
                            " | [A] Add Product" +
                            " | [U] Update Product" +
                            " | [D] Delete Product " +
                            "\n" +
                            " [F] Filter " + ((predicate != null) ? "( [R] Reset )" : "") +
                            " | [S] Sort" +
                            " | [T] " + (isAsc ? "Ascending" : "Descending") +
                            " | [Q] Back" +
                            " | [X] Exit"
            );
            print.print("==> ");
            
            switch (inputReader.getInput(Character.class, '\0')) {
                case 'V', 'v' -> viewDetailsProduct();
                case 'A', 'a' -> addProduct();
                case 'U', 'u' -> updateProduct();
                case 'D', 'd' -> deleteProduct();
                case 'F', 'f' -> predicate = this.filterMenuListProducts();
                case 'R', 'r' -> predicate = null;
                case 'S', 's' -> {
                    print.println("Available sort fields:");
                    print.println("1. by Name | 2. by Price | 3. by Stock");
                    print.print("==> ");
                    sortField = switch (inputReader.getInput(Integer.class, 0)) {
                        case 1 -> PRODUCT_SORT_FIELD.NAME;
                        case 2 -> PRODUCT_SORT_FIELD.PRICE;
                        case 3 -> PRODUCT_SORT_FIELD.STOCK;
                        default -> null;
                    };
                }
                case 'T', 't' -> isAsc = !isAsc;
                case 'Q', 'q' -> {
                    return MenuProductResult.BACK;
                }
                case 'X', 'x' -> {
                    return MenuProductResult.EXIT;
                }
                default -> print.println("Invalid choice.");
            }
        }
    }
    
    public void viewDetailsProduct() {
        print.print("Enter product id: ");
        long id = inputReader.getInput(Long.class, 0L);
        
        Product product = productService.getProductById(id);
        if (product == null) {
            print.println("Product not found.");
        } else {
            detailProduct(product);
        }
        inputReader.getInput(String.class);
    }
    
    public void addProduct() {
        print.print("Enter product name: ");
        String name = inputReader.getInput(String.class, print);
        print.print("Enter product price: ");
        BigDecimal price = inputReader.getInput(BigDecimal.class, print);
        print.print("Enter product stock: ");
        Integer stock = inputReader.getInput(Integer.class, print);
        
        Product newProduct = productService.createProduct(name, price, stock);
        print.println("Product added: " + newProduct);
    }
    
    public void updateProduct() {
        print.print("Enter product ID to update: ");
        long id = inputReader.getInput(Long.class, 0L);
        Product product = productService.getProductById(id);
        if (product == null) {
            print.println("Product not found.");
            return;
        }
        detailProduct(product);
        print.println();
        print.print("Enter new name (" + product.getName() + "): ");
        String name = inputReader.getInput(String.class);
        print.print("Enter new price (" + product.getPrice() + "): ");
        BigDecimal price = inputReader.getInput(BigDecimal.class);
        print.print("Enter new stock (" + product.getStock() + "): ");
        Integer stock = inputReader.getInput(Integer.class);
        
        Product updatedProduct = productService.updateProduct(id, name, price, stock);
        print.println("Product updated: " + updatedProduct);
    }
    
    public void deleteProduct() {
        print.print("Enter product ID to delete: ");
        long id = inputReader.getInput(Long.class, 0L);
        
        Product product = productService.getProductById(id);
        
        if (product != null) {
            print.println("=[ " + product.getId() + " | " + product.getName() + " ]=");
            print.print("type 'n' for cancel, press enter to confirm => ");
            
            Character confirm = inputReader.getInput(Character.class);
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
        return switch (inputReader.getInput(Integer.class, 0)) {
            case 1 -> {
                print.print("Search by name: ");
                String name = inputReader.getInput(String.class);
                yield product -> product.getName().toLowerCase().contains(name.toLowerCase());
            }
            case 2 -> {
                print.println("Filter range price:");
                print.print("Min price: ");
                BigDecimal minPrice = inputReader.getInput(BigDecimal.class, BigDecimal.ZERO);
                print.print("Max price (if null, will equal min): ");
                BigDecimal maxPrice = inputReader.getInput(BigDecimal.class);
                
                yield product -> (maxPrice == null)
                        ? product.getPrice().compareTo(minPrice) == 0
                        : product.getPrice().compareTo(minPrice) >= 0 && product.getPrice().compareTo(maxPrice) <= 0;
            }
            case 3 -> {
                print.println("Filter range stock:");
                print.print("Min stock (default 0): ");
                int minStock = inputReader.getInput(Integer.class, 0);
                print.print("Max stock (if null, will equal min): ");
                Integer maxStock = inputReader.getInput(Integer.class);
                
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
        print.println("Created at: " + product.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm")));
    }
    
}
