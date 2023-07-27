package com.elves.dscommerce.services;

import com.elves.dscommerce.dto.ProductDTO;
import com.elves.dscommerce.dto.ProductMinDTO;
import com.elves.dscommerce.entities.Product;
import com.elves.dscommerce.repositories.ProductRepository;
import com.elves.dscommerce.services.exceptions.ResourceNotFoundException;
import com.elves.dscommerce.tests.ProductFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    private long existingProductId;
    private long nonExistingProductId;

    private String productName;
    private Product product;
    private ProductDTO dto;

    private List<Product> list;
    private Pageable pageable;
    private Page<Product> page;

    @BeforeEach
    void setUp() throws Exception {
        existingProductId = 1l;
        nonExistingProductId = 1000L;

        productName = "PS5";
        product = ProductFactory.createProduct(productName);

        list= new ArrayList<>();

        list.add(product);
        list.add(ProductFactory.createProduct("PS5-Plus"));

        pageable = PageRequest.of(0, 12);
        page= new PageImpl<>(list,pageable,pageable.getPageNumber());


        Mockito.when(repository.findById(existingProductId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistingProductId)).thenReturn(Optional.empty());
        Mockito.when(repository.searchByName(productName, pageable)).thenReturn(page);

    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = service.findById(existingProductId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingProductId);
        Assertions.assertEquals(result.getName(), product.getName());

    }

    @Test
    public void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            ProductDTO result = service.findById(nonExistingProductId);
        });
    }

    @Test
    public void findAllShouldReturnPagedProductMinDTO() {
        Pageable pageable1= PageRequest.of(0,12);
        String name = "PS5";

        Page<ProductMinDTO> result=service.findAll(name,pageable1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.get().toList().get(0).getName(),product.getName());
        Assertions.assertEquals(result.stream().toList().get(1).getName(),"PS5-Plus");

    }

}
