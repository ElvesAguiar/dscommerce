package com.elves.dscommerce.comtrollers.it;

import com.elves.dscommerce.dto.OrderDTO;
import com.elves.dscommerce.entities.*;
import com.elves.dscommerce.tests.ProductFactory;
import com.elves.dscommerce.tests.UserFactory;
import com.elves.dscommerce.tests.utils.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private String clientUserName, clientPassword, adminUserName, adminPassword;

    private String adminToken, clientToken, invalidToken;

    private long existingOrderId, nonExistingOrderId;

    private Order order;
    private OrderDTO dto;

    private User user;


    @BeforeEach
    void setUp() throws Exception {
        clientUserName = "maria@gmail.com";
        clientPassword = "123456";

        adminUserName = "alex@gmail.com";
        adminPassword = "123456";

        existingOrderId = 1L;
        nonExistingOrderId = 100L;

        adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUserName, adminPassword);
        clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUserName, clientPassword);
        invalidToken = adminToken + "xpto";//invalid


        user = UserFactory.createClientUser();
        order = new Order(null, Instant.now(), OrderStatus.WAITING_PAYMENT,user,null);

        Product product = ProductFactory.createProduct();
        OrderItem orderItem = new OrderItem(order,product,2,10.0);

        order.getItems().add(orderItem);



    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndUserAdmin() throws Exception {

        ResultActions result = mockMvc.perform(get("/orders/{id}",existingOrderId)
                .header("Authorization","Bearer "+adminToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(1L));
        result.andExpect(jsonPath("$.moment").value("2022-07-25T13:00:00Z"));
        result.andExpect(jsonPath("$.orderStatus").value("PAID"));
        result.andExpect(jsonPath("$.client").exists());
        result.andExpect(jsonPath("$.client.name").value("Maria Brown"));
        result.andExpect(jsonPath("$.payment").exists());
        result.andExpect(jsonPath("$.items").exists());
        result.andExpect(jsonPath("$.items[1].name").value("Macbook Pro"));
        result.andExpect(jsonPath("$.total").value(1431.0));


    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndUserClient() throws Exception {

        ResultActions result = mockMvc.perform(get("/orders/{id}",existingOrderId)
                        .header("Authorization","Bearer "+clientToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(1L));
        result.andExpect(jsonPath("$.moment").value("2022-07-25T13:00:00Z"));
        result.andExpect(jsonPath("$.orderStatus").value("PAID"));
        result.andExpect(jsonPath("$.client").exists());
        result.andExpect(jsonPath("$.client.name").value("Maria Brown"));
        result.andExpect(jsonPath("$.payment").exists());
        result.andExpect(jsonPath("$.items").exists());
        result.andExpect(jsonPath("$.items[1].name").value("Macbook Pro"));
        result.andExpect(jsonPath("$.total").value(1431.0));


    }
    @Test
    public void findByIdShouldReturnForbiddenWhenIdExistsAndUserClientAndOrderDoesNotBelongToUser() throws Exception {

        Long otherOrderId = 2L;

        ResultActions result = mockMvc.perform(get("/orders/{id}",otherOrderId)
                        .header("Authorization","Bearer "+clientToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isForbidden());

    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdExistsAndUserAdmin() throws Exception {


        ResultActions result = mockMvc.perform(get("/orders/{id}",nonExistingOrderId)
                        .header("Authorization","Bearer "+adminToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isNotFound());

    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdExistsAndUserClient() throws Exception {


        ResultActions result = mockMvc.perform(get("/orders/{id}",nonExistingOrderId)
                        .header("Authorization","Bearer "+clientToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isNotFound());

    }

    @Test
    public void findByIdShouldReturnUnauthorizedWhenIdExistsAndTokenIsInvalid() throws Exception {


        ResultActions result = mockMvc.perform(get("/orders/{id}",existingOrderId)
                        .header("Authorization","Bearer "+invalidToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnauthorized());

    }





}
