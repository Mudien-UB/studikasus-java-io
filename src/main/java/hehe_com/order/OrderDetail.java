package hehe_com.order;

import hehe_com.product.Product;

public class OrderDetail {
    
    private Product product;
    private int amount;
    
    public OrderDetail(Product product, int amount) {
        this.product = product;
        this.amount = amount;
    }
    
    public Product getProduct() { return product; }
    public int getAmount() { return amount; }
    
    public void addAmount(int add) {
        this.amount += add;
    }
    
    @Override
    public String toString() {
        return product.getName() + " x " + amount;
    }
}
