package com.elves.dscommerce.entities;


import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_order_item")
public class OrderItem {
    @EmbeddedId
    private OrderItemPK id = new OrderItemPK();

    private Integer quantity;
    private double price;

   public OrderItem(){

    }

    public OrderItem(Order order, Product product ,Integer quantity, double price) {
       this.id.setOrder(order);
       this.id.setProduct(product);
       this.quantity = quantity;
        this.price = price;
    }

    public Order getOrder(){
       return id.getOrder();
    }

    public void setOrder(Order order){
        id.setOrder(order);
    }

    public Product getProduct(){
       return id.getProduct();
    }

    public void setProduct(Product product){
        id.setProduct(product);
    }


    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
