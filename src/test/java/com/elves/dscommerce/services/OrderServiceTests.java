package com.elves.dscommerce.services;

import com.elves.dscommerce.dto.OrderDTO;
import com.elves.dscommerce.entities.Order;
import com.elves.dscommerce.entities.OrderItem;
import com.elves.dscommerce.entities.Product;
import com.elves.dscommerce.entities.User;
import com.elves.dscommerce.repositories.OrderItemRepository;
import com.elves.dscommerce.repositories.OrderRepository;
import com.elves.dscommerce.repositories.ProductRepository;
import com.elves.dscommerce.services.exceptions.ForbiddenException;
import com.elves.dscommerce.services.exceptions.ResourceNotFoundException;
import com.elves.dscommerce.tests.OrderFactory;
import com.elves.dscommerce.tests.ProductFactory;
import com.elves.dscommerce.tests.UserFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
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

    private User admin, client;

    private Long existingProductId, nonExistingProductId;
    private Product product;


    @BeforeEach
    void setUp() throws Exception {
        existingOrderId = 1L;
        nonExistingOrderId = 1000L;
        admin = UserFactory.createCustomAdminUser(1L, "Bob");
        client = UserFactory.createCustomClientUser(2L, "maria");

        order = OrderFactory.createOrder(client);

        dto = new OrderDTO(order);
        existingProductId = 1L;
        nonExistingProductId = 1000L;
        product = ProductFactory.createProduct();

        Mockito.when(repository.findById(existingOrderId)).thenReturn(Optional.of(order));
        Mockito.when(repository.findById(nonExistingOrderId)).thenReturn(Optional.empty());
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(order);
        Mockito.when(userService.authenticated()).thenReturn(order.getClient());
        Mockito.when(productRepository.getReferenceById(existingProductId)).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(nonExistingProductId))
                .thenThrow(ResourceNotFoundException.class);
        Mockito.when(orderItemRepository.saveAll(ArgumentMatchers.any())).thenReturn(new ArrayList<>(order.getItems()));

    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged() {
        Mockito.doNothing().when(authService).validateSelfOrAdmin(ArgumentMatchers.any());
        OrderDTO result = service.findById(existingOrderId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingOrderId);

    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndSelfClientLogged() {
        Mockito.doNothing().when(authService).validateSelfOrAdmin(client.getId());
        OrderDTO result = service.findById(existingOrderId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingOrderId);

    }

    @Test
    public void findByIdShouldThrowsForbiddenExceptionWhenIdExistsAndOtherClientLogged() {
        Mockito.doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(ArgumentMatchers.any());

        Assertions.assertThrows(ForbiddenException.class, () -> {
            OrderDTO result = service.findById(existingOrderId);
        });

    }

    @Test
    public void findByIdShouldThrowsResourceNotFoundExceptionIdDoesNotExist() {
        Mockito.doNothing().when(authService).validateSelfOrAdmin(ArgumentMatchers.any());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            OrderDTO result = service.findById(nonExistingOrderId);

        });
    }

    @Test
    public void insertShouldReturnOrderDTOAdminLogged() {
        Mockito.when(userService.authenticated()).thenReturn(admin);

        OrderDTO result = service.insert(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingOrderId);
    }

    @Test
    public void insertShouldReturnOrderDTOAdmiClientLogged() {
        Mockito.when(userService.authenticated()).thenReturn(client);

        OrderDTO result = service.insert(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingOrderId);
    }

    @Test
    public void insertShouldThroesUserNotFoundExceptionWhenUserNotLogged() {
        Mockito.doThrow(UsernameNotFoundException.class).when(userService).authenticated();
        order.setClient(new User());
        dto = new OrderDTO(order);
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            OrderDTO result = service.insert(dto);
        });
    }

    @Test
    public void insertShouldThrowsResourceNotFoundExceptionOrderProductIdDoesNotExist() {
        Mockito.when(userService.authenticated()).thenReturn(client);
        product.setId(nonExistingProductId);

        OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
        order.getItems().add(orderItem);

        OrderDTO orderDTO = new OrderDTO(order);

        Assertions.assertThrows(ResourceNotFoundException.class, () ->{
            OrderDTO result = service.insert(orderDTO);
        });
    }

}
