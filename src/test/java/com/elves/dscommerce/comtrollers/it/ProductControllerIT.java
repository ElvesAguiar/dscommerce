package com.elves.dscommerce.comtrollers.it;

import com.elves.dscommerce.dto.CategoryDTO;
import com.elves.dscommerce.dto.ProductDTO;
import com.elves.dscommerce.entities.Product;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    private String productName;

    private String clientUserName,clientPassword,adminUserName,adminPassword;
    private long existingProductId, nonExistingProductId, dependentProductId;
    private String adminToken,clientToken,invalidToken;

    private Product product;
    private ProductDTO dto;


    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        clientUserName="maria@gmail.com";
        clientPassword="123456";

        adminUserName="alex@gmail.com";
        adminPassword="123456";

        productName = "mac";

        existingProductId=2L;
        nonExistingProductId=100L;
        dependentProductId=3L;

        adminToken=tokenUtil.obtainAccessToken(mockMvc,adminUserName,adminPassword);
        clientToken=tokenUtil.obtainAccessToken(mockMvc,clientUserName,clientPassword);
        invalidToken=adminToken+"xpto";//invalid

         product =
                new Product(null, "Ps5",
                        "Lorem ipsum, dolor sit amet consectetur " +
                        "adipisicing elit.Qui ad, adipisci illum ipsam velit et odit eaque reprehenderit" +
                        " ex maxime delectus dolore labore, quisquam quae tempora natus esse" +
                        " aliquam veniam doloremque quam minima culpa alias maiores commodi." +
                        " Perferendis enim",
                        1000.0,
                        "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"

                );
         dto=new ProductDTO(product);
        dto.getCategories().add(new CategoryDTO(2L, null));
        dto.getCategories().add(new CategoryDTO(3L, null));


    }

    @Test
    public void findAllShouldReturnPageWhenNameParamIsNotEmpty() throws Exception {
        ResultActions result = mockMvc
                .perform(get("/products?name={productName}", productName)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content[0].id").value(3L));
        result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
        result.andExpect(jsonPath("$.content[0].price").value(1250.0));
        result.andExpect(jsonPath("$.content[0].imgUrl")
                .value("https://raw.githubusercontent.com/devsuperior" +
                       "/dscatalog-resources/master/backend/img/3-big.jpg"));
    }

    @Test
    public void findAllShouldReturnPageWhenNameParamIsEmpty() throws Exception {


        ResultActions result = mockMvc
                .perform(get("/products")
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content[0].id").value(1L));
        result.andExpect(jsonPath("$.content[0].name").value("The Lord of the Rings"));
        result.andExpect(jsonPath("$.content[0].price").value(90.5));
        result.andExpect(jsonPath("$.content[0].imgUrl")
                .value("https://raw.githubusercontent.com/devsuperior" +
                       "/dscatalog-resources/master/backend/img/1-big.jpg"));
    }

    @Test
    public void insertShouldReturnProductDTOCreatedWhenValidDataAndUserAdmin() throws Exception {



        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc
                .perform(post("/products").header("Authorization","Bearer "+adminToken).content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));


        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").value(26));
        result.andExpect(jsonPath("$.name").value("Ps5"));
        result.andExpect(jsonPath("$.description")
                .value("Lorem ipsum, dolor sit amet consectetur adipisicing elit.Qui ad, adipisci illum ipsam velit et odit eaque reprehenderit ex maxime delectus dolore labore, quisquam quae tempora natus esse aliquam veniam doloremque quam minima culpa alias maiores commodi. Perferendis enim"));

        result.andExpect(jsonPath("$.price").value(1000.0));
        result.andExpect(jsonPath("$.imgUrl").value("https://raw.githubusercontent.com/devsuperior" +
                                                    "/dscatalog-resources/master/backend/img/1-big.jpg"));

        result.andExpect(jsonPath("$.categories[0].id").value(2));
        result.andExpect(jsonPath("$.categories[1].id").value(3));
    }

    @Test
    public void insertShouldReturn422WhenUserAdminAndInvalidDataInNameField() throws Exception {

        dto.setName("PS");

        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc
                .perform(post("/products").header("Authorization","Bearer "+adminToken).content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print());


        result.andExpect(status().isUnprocessableEntity());

    }

    @Test
    public void insertShouldReturn422WhenUserAdminAndInvalidDataOnDescriptionField() throws Exception {

        dto.setDescription("");

        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc
                .perform(post("/products").header("Authorization","Bearer "+adminToken).content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));


        result.andExpect(status().isUnprocessableEntity());

    }

    @Test
    public void insertShouldReturn422WhenUserAdminAndPriceIsNegative() throws Exception {

        dto.setPrice(-3999.0);

        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc
                .perform(post("/products")
                        .header("Authorization","Bearer "+adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));


        result.andExpect(status().isUnprocessableEntity());

    }

    @Test
    public void insertShouldReturn422WhenUserAdminAndPriceIsZero() throws Exception {

        dto.setPrice(0.0);

        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc
                .perform(post("/products")
                        .header("Authorization","Bearer "+adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));


        result.andExpect(status().isUnprocessableEntity());

    }
    @Test
    public void insertShouldReturn422WhenUserAdminAndCategoriesIsNull() throws Exception {

        dto.getCategories().clear();

        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc
                .perform(post("/products")
                        .header("Authorization","Bearer "+adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));


        result.andExpect(status().isUnprocessableEntity());

    }

    @Test
    public void insertShouldReturn403WhenUserClient() throws Exception {


        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc
                .perform(post("/products")
                        .header("Authorization","Bearer "+clientToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));


        result.andExpect(status().isForbidden());

    }

    @Test
    public void insertShouldReturn401WhenInvalidToken() throws Exception {


        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc
                .perform(post("/products")
                        .header("Authorization","Bearer "+invalidToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));


        result.andExpect(status().isUnauthorized());

    }

    @Test
    public void deleteShouldReturnNoContentWhenUserAdminAndProductIdExists() throws Exception {

        ResultActions result = mockMvc
                .perform(delete("/products/{id}",existingProductId)
                        .header("Authorization","Bearer "+adminToken)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenUserAdminAndProductIdDoNotExist() throws Exception {

        ResultActions result = mockMvc
                .perform(delete("/products/{id}",nonExistingProductId)
                        .header("Authorization","Bearer "+adminToken)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteShouldReturnBadRequestWhenUserAdminAndProductIdIsDependent() throws Exception {

        ResultActions result = mockMvc
                .perform(delete("/products/{id}",dependentProductId)
                        .header("Authorization","Bearer "+adminToken)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void deleteShouldReturnForbiddenWhenUserClient() throws Exception {

        ResultActions result = mockMvc
                .perform(delete("/products/{id}",existingProductId)
                        .header("Authorization","Bearer "+clientToken)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isForbidden());
    }

    @Test
    public void deleteShouldReturnForbiddenWhenUserHasNoValidToken() throws Exception {

        ResultActions result = mockMvc
                .perform(delete("/products/{id}",existingProductId)
                        .header("Authorization","Bearer "+invalidToken)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());
    }



}
