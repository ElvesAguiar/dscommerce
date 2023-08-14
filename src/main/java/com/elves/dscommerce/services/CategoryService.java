package com.elves.dscommerce.services;

import com.elves.dscommerce.dto.CategoryDTO;
import com.elves.dscommerce.entities.Category;
import com.elves.dscommerce.repositories.CategoryRepository;
import com.elves.dscommerce.services.exceptions.DatabaseException;
import com.elves.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    ModelMapper modelMapper = new ModelMapper();

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> result = repository.findAll();

        return result.stream().map(CategoryDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        if (repository.findById(id).isEmpty()) throw new ResourceNotFoundException("Recurso não encontrado!");

        CategoryDTO dto = modelMapper.map(repository.findById(id), CategoryDTO.class);

        return dto;
    }

    @Transactional
    public void delete(Long id) {
        if (repository.findById(id).isEmpty()) throw new ResourceNotFoundException("Recurso não encontrado!");
        try {

            if(!repository.findById(id).get().getProducts().isEmpty()) throw new DatabaseException("Falha de Integridade referencial!");
            repository.deleteById(id);

        } catch (DatabaseException e) {
            throw new DatabaseException("Falha de Integridade referencial!");
        }

    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        entity.setName(dto.getName());
        repository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }


}
