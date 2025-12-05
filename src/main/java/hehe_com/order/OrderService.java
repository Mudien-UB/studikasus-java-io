package hehe_com.order;

import hehe_com.product.Product;
import hehe_com.product.ProductRepository;
import hehe_com.product.ProductService;
import hehe_com.user.UserRepository;
import hehe_com.user.Users;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OrderService {
    
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }
    
    public OrderDetail addItem(Long productId, int amount) {
        if (productId == null || amount <= 0) {
            throw new IllegalArgumentException("Invalid product or amount");
        }
        
        Product product = productRepository.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }
        
        return new OrderDetail(product, amount);
    }
    
    public Order submitOrder(Users user, List<OrderDetail> items) {
        
        if (user == null) {
            throw new IllegalArgumentException("User not logged in");
        }
        
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order is empty");
        }
        
        BigDecimal total = items.stream()
                .map(i -> i.getProduct().getPrice().multiply(BigDecimal.valueOf(i.getAmount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Order order = new Order(
                this.generateIdOrder(),
                items,
                total,
                user,
                LocalDateTime.now()
        );
        
        orderRepository.save(order);
        return order;
    }
    
    public List<Order> LoadAllOrders(Users user) {
        if (user == null) {
            throw new IllegalArgumentException("User not logged in");
        }
        try {
        
        List<Order> orders = new ArrayList<>();
        List<Map<String, Object>> rawOrderList = orderRepository.loadAllWithUser(user);
        for (var rawOrder : rawOrderList) {
            String id = rawOrder.get("id").toString();
            BigDecimal totalPrice = new BigDecimal(rawOrder.get("totalPrice").toString());
            Users userOrder = userRepository.findByUsername(rawOrder.get("user").toString());
            LocalDateTime orderDate = LocalDateTime.parse(rawOrder.get("orderTime").toString());
            var itemsRaw = (List<Map<String, String>>) rawOrder.get("items");
            List<OrderDetail> items = itemsRaw.stream().map(i -> {
                try{
                Product product = productRepository.findById(Long.parseLong(i.get("product").toString()));
                Integer amount = Integer.parseInt(i.get("amount").toString());
                return new OrderDetail(product, amount);
                }catch(Exception e){
                    return null;
                }
            }).toList();
            
            Order order = new Order(
                    id,
                    items,
                    totalPrice,
                    userOrder,
                    orderDate
            );
            orders.add(order);
        }
        
        return orders;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    
    private String generateIdOrder() {
        return UUID.randomUUID().toString();
    }
}
