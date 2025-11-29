package hehe_com.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class ProductService {
    
    private final ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    
    public Product createProduct(String name, BigDecimal price, int stock) {
        
        try {
            long id = productRepository.generateIDProduct();
            
            if(name == null || name.isBlank() || price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("field can't be empty");
            }
            
            Product newProduct = new Product(id, name,price,stock, LocalDateTime.now());
            
            return productRepository.saveNew(newProduct);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public Product updateProduct(long id, String name, BigDecimal price, Integer stock) {
        try{
            
            Product product = productRepository.findById(id);
            product.setName((name == null) ? product.getName() : name);
            product.setPrice((price == null) ? product.getPrice() : price);
            product.setStock(stock == 0 ? product.getStock() : stock);
            
            return productRepository.saveUpdated(product);
        }catch(RuntimeException e){
            throw new RuntimeException(e);
        }
    }
    
    public boolean deleteProduct(long id) {
        try{
            Product product = productRepository.findById(id);
            if(product == null) return false;
            return productRepository.delete(product);
        }catch(RuntimeException e){
            throw new RuntimeException(e);
        }
    }
    
    public Product getProductById(long id) {
        try{
            Product product = productRepository.findById(id);
            if(product == null) return null;
            return product;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    
    public List<Product> getAllProducts(Predicate<Product> predicate,PRODUCT_SORT_FIELD sortField, Boolean isAsc) {
        try{
            List<Product> products;
            if(predicate == null ) products = productRepository.loadAll();
            else products = productRepository.loadAll().stream().filter(predicate).toList();
            
            if(sortField != null){
                Comparator<Product> comparator = switch (sortField){
                    case NAME ->  Comparator.comparing(Product::getName);
                    case PRICE -> Comparator.comparing(Product::getPrice);
                    case STOCK -> Comparator.comparing(Product::getStock);
                    case UPDATED_AT ->  Comparator.comparing(Product::getUpdatedAt);
                };
                return products.stream().sorted((isAsc != null && !isAsc) ? comparator.reversed() : comparator).toList();
            }else{
                return products;
            }
            
        }catch(RuntimeException e){
            throw new RuntimeException(e);
        }
    }
}
