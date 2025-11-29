package hehe_com.product;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class ProductRepositoryTest {
    
    
    @Test
    void testCreateAndLoad() {
    
        ProductRepository productRepository = new ProductRepository(Path.of("data/products"));
        
        var id = productRepository.generateIDProduct();
        
        Assertions.assertInstanceOf(Long.class,id);
        
        Product newProduct = new Product(id,"roti", BigDecimal.valueOf(1000), 10, LocalDateTime.now());
        
        Product savedProduct = productRepository.saveNew(newProduct );
        
        Assertions.assertNotNull(savedProduct);
        
        List<Product> productList = productRepository.loadAll();
        
        Assertions.assertNotNull(productList);
        
        Assertions.assertEquals(savedProduct, productList.getFirst());
    }
}
