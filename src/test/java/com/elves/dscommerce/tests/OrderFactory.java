package com.elves.dscommerce.tests;

import com.elves.dscommerce.entities.*;

import java.time.Instant;

public class OrderFactory {

    public static Order createOrder(User user) {
        Category category = CategoryFactory.createCategory();
        Order order = new Order();
        order.setId(1L);
        order.setMoment(Instant.now());
        order.setPayment(null);
        order.setStatus(OrderStatus.WAITING_PAYMENT);
        order.setClient(user);

        Product product = ProductFactory.createProduct();
        OrderItem orderItem = new OrderItem(order,product,2,10.0);
        order.getItems().add(orderItem);


        return order;
    }


}
