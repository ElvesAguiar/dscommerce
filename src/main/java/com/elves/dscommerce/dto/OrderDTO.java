package com.elves.dscommerce.dto;

import com.elves.dscommerce.entities.Order;
import com.elves.dscommerce.entities.OrderStatus;
import jakarta.validation.constraints.NotEmpty;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {
    private Long id;
    private Instant moment;
    private OrderStatus orderStatus;
    private ClientDTO client;
    private PaymentDTO payment;
    @NotEmpty(message = "Deve ter pelo menos um item")
    private List<OrderItemDTO> items = new ArrayList<>();

    public OrderDTO(Long id, Instant moment, OrderStatus orderStatus, ClientDTO client, PaymentDTO payment) {
        this.id = id;
        this.moment = moment;
        this.orderStatus = orderStatus;
        this.client = client;
        this.payment = payment;
    }

    public OrderDTO(Order entity) {
        this.id = entity.getId();
        this.moment = entity.getMoment();
        this.orderStatus = entity.getStatus();
        this.client = new ClientDTO(entity.getClient());
        this.payment = (entity.getPayment() == null) ? null : new PaymentDTO(entity.getPayment());
        entity.getItems().forEach(x -> items.add(new OrderItemDTO(x)));
    }

    public double getTotal() {
        return items.stream()
                .mapToDouble(OrderItemDTO::getSubTotal)
                .reduce(0.0, (subtotal1, subtotal2) -> subtotal1 + subtotal2);
    }

    public Long getId() {
        return id;
    }

    public Instant getMoment() {
        return moment;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public ClientDTO getClient() {
        return client;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }
}
