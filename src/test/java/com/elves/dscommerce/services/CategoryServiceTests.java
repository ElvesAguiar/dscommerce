package com.elves.dscommerce.services;

import com.elves.dscommerce.dto.CategoryDTO;
import com.elves.dscommerce.entities.Category;
import com.elves.dscommerce.repositories.CategoryRepository;
import com.elves.dscommerce.tests.CategoryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

    @InjectMocks
    private CategoryService service;

    @Mock
    private CategoryRepository repository;

    private ModelMapper mapper= new ModelMapper();

    private Category category;
    private CategoryDTO dto;
    private Long existingId, nonExistingId, referenceId;
    private List<Category> list;


    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        referenceId = 5L;


        category = CategoryFactory.createCategory();
        dto=mapper.map(category, CategoryDTO.class);

        list = new ArrayList<>();
        list.add(category);

        Mockito.when(repository.findAll()).thenReturn(list);
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(category));
    }

    @Test
    public void findAllShouldReturnListCategoryDTO() {

        List<CategoryDTO> result = service.findAll();

        Assertions.assertEquals(result.size(), 1);
        Assertions.assertEquals(result.get(0).getId(), category.getId());
        Assertions.assertEquals(result.get(0).getName(), category.getName());
    }

    @Test
    public void findByIdShouldReturnCategoryDTOWhenIdExists() {

        CategoryDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
        Assertions.assertEquals(result.getName(), category.getName());
    }

}
