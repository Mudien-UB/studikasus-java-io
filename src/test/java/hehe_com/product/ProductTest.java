package hehe_com.product;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductTest {
    
    @Test
    void equalsTestTrue() {
        Product product1 = new Product(1,"test", BigDecimal.valueOf(10_000_000), 1_000, LocalDateTime.now());
        Product product2 = new  Product(1,"test", BigDecimal.valueOf(10_000_000), 1_000, LocalDateTime.now());
        
        Assertions.assertNotNull(product1);
        Assertions.assertEquals(product2, product1);
        Assertions.assertNotSame(product2, product1);
    }
}
