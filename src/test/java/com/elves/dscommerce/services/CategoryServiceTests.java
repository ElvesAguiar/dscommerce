package com.elves.dscommerce.services;

import com.elves.dscommerce.dto.CategoryDTO;
import com.elves.dscommerce.entities.Category;
import com.elves.dscommerce.repositories.CategoryRepository;
import com.elves.dscommerce.services.exceptions.DatabaseException;
import com.elves.dscommerce.services.exceptions.ResourceNotFoundException;
import com.elves.dscommerce.tests.CategoryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;
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
    private Long existingId, nonExistingId,referencedId;
    private List<Category> list;


    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        referencedId=3L;


        category = CategoryFactory.createCategory();
        dto=mapper.map(category, CategoryDTO.class);

        list = new ArrayList<>();
        list.add(category);

        Mockito.when(repository.findAll()).thenReturn(list);
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(category));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.when(repository.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(category);
        Mockito.doThrow(DatabaseException.class).when(repository).deleteById(referencedId);
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

    @Test
    public void findByIdShouldReturn404WhenIdDoesNotExists(){


      Assertions.assertThrows(ResourceNotFoundException.class,()->{
          CategoryDTO result = service.findById(nonExistingId);
      });

    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {

        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(referencedId);
        });
    }


    @Test
    public void insertShouldReturnCategoryDTOWheIdExists() {
        dto.setId(null);

        CategoryDTO result= service.insert(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getName(),category.getName());

    }

}
