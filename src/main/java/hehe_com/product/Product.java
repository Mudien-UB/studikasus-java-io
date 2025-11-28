package hehe_com.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class Product {
    
    private final long id;
    
    private String name;
    
    private BigDecimal price;
    
    private int stock;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    public Product(long id, String name, BigDecimal price, int stock, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    public long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public int getStock() {
        return stock;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return getId() == product.getId() && Objects.equals(getName(), product.getName());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
