package com.elves.dscommerce.services;

import com.elves.dscommerce.dto.OrderDTO;
import com.elves.dscommerce.entities.Order;
import com.elves.dscommerce.entities.OrderItem;
import com.elves.dscommerce.entities.OrderStatus;
import com.elves.dscommerce.entities.Product;
import com.elves.dscommerce.repositories.OrderItemRepository;
import com.elves.dscommerce.repositories.OrderRepository;
import com.elves.dscommerce.repositories.ProductRepository;
import com.elves.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {

        return new OrderDTO(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado!")));
    }

    @Transactional(readOnly = true)
    public OrderDTO inset(OrderDTO dto) {
        Order order = new Order();
        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_PAYMENT);

        order.setClient(userService.authenticated());

        dto.getItems().forEach(x -> {
            Product product = productRepository.getReferenceById(x.getProductId());
            OrderItem item = new OrderItem(order, product, x.getQuantity(), product.getPrice());
            order.getItems().add(item);
        });

        repository.save(order);
        orderItemRepository.saveAll(order.getItems());
        return new OrderDTO(order);
    }


}
