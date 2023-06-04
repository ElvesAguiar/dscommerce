package com.elves.dscommerce.services;

import com.elves.dscommerce.dto.CategoryDTO;
import com.elves.dscommerce.entities.Category;
import com.elves.dscommerce.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> result = repository.findAll();

        return result.stream().map(CategoryDTO::new).toList();
    }


}
