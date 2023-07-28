package com.elves.dscommerce.services;

import com.elves.dscommerce.dto.OrderDTO;
import com.elves.dscommerce.entities.Order;
import com.elves.dscommerce.repositories.OrderItemRepository;
import com.elves.dscommerce.repositories.OrderRepository;
import com.elves.dscommerce.repositories.ProductRepository;
import com.elves.dscommerce.services.exceptions.ResourceNotFoundException;
import com.elves.dscommerce.tests.OrderFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

    @InjectMocks
    private OrderService service;

    @Mock
    private OrderRepository repository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;


    private long existingOrderId;
    private long nonExistingOrderId;


    private Order order;
    private OrderDTO dto;


    @BeforeEach
    void setUp() throws Exception {
        existingOrderId = 1L;
        nonExistingOrderId = 1000L;
        order = OrderFactory.createOrder();

        Mockito.when(repository.findById(existingOrderId)).thenReturn(Optional.of(order));
        Mockito.when(repository.findById(nonExistingOrderId)).thenReturn(Optional.empty());
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(order);
        Mockito.doNothing().when(authService).validateSelfOrAdmin(order.getClient().getId());
        Mockito.when(userService.authenticated()).thenReturn(order.getClient());

    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExists() {
        OrderDTO result = service.findById(existingOrderId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingOrderId);

    }

    @Test
    public void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            OrderDTO result = service.findById(nonExistingOrderId);
        });
    }


    @Test
    public void insertShouldReturnOrderDTO() {
        OrderService serviceSpy = Mockito.spy(service);
        OrderDTO result = serviceSpy.insert(new OrderDTO(order));


        Assertions.assertNotNull(result);

    }


}
