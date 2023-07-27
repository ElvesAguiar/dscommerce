package com.elves.dscommerce.services;

import com.elves.dscommerce.dto.ProductDTO;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

    @BeforeEach
    void setUp() throws Exception{
        existingProductId=1l;
        nonExistingProductId=1000L;

        productName="PS5";
        product= ProductFactory.createProduct(productName);

        Mockito.when(repository.findById(existingProductId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistingProductId)).thenThrow(ResourceNotFoundException.class);

    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists(){
        ProductDTO result= service.findById(existingProductId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingProductId);
        Assertions.assertEquals(result.getName(),product.getName()) ;
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdNonExists(){

        Assertions.assertThrows(ResourceNotFoundException.class,()->{
            ProductDTO result= service.findById(nonExistingProductId);
        });
    }

}
