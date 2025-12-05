package hehe_com.order;

import hehe_com.user.Users;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Order {
    
    private String id;
    private List<OrderDetail> items;
    private BigDecimal totalPrice;
    private Users user;
    private LocalDateTime orderTime;
    
    public Order(String id, List<OrderDetail> items, BigDecimal totalPrice, Users user, LocalDateTime orderTime) {
        this.id = id;
        this.items = items;
        this.totalPrice = totalPrice;
        this.user = user;
        this.orderTime = orderTime;
    }
    
    @Override
    public String toString() {
        return "[ " +
                "id='" + id + '\'' +
                ", items=" + items +
                ", totalPrice=" + totalPrice +
                ", user=" + user.getUsername() +
                ", orderTime=" + orderTime.format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")) +
                " ]";
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public List<OrderDetail> getItems() { return items; }
    public void setItems(List<OrderDetail> items) { this.items = items; }
    
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    
    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }
    
    public LocalDateTime getOrderTime() { return orderTime; }
    public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }
}
