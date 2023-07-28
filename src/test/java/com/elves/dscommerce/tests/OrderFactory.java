package com.elves.dscommerce.tests;

import com.elves.dscommerce.entities.*;

import java.time.Instant;

public class OrderFactory {

    public static Order createOrder() {
        Category category = CategoryFactory.createCategory();
        Order order = new Order();
        order.setId(1L);
        order.setMoment(Instant.now());
        order.setPayment(null);
        order.setStatus(OrderStatus.WAITING_PAYMENT);
        User user = new User(1L,"ana","ana@gmail.com",null,null,"123456");

        order.setClient(user);
        return order;
    }


}
