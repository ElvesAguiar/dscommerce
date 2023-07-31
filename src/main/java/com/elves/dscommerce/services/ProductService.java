package com.elves.dscommerce.services;

import com.elves.dscommerce.dto.ProductDTO;
import com.elves.dscommerce.dto.ProductMinDTO;
import com.elves.dscommerce.entities.Category;
import com.elves.dscommerce.entities.Product;
import com.elves.dscommerce.repositories.ProductRepository;
import com.elves.dscommerce.services.exceptions.DatabaseException;
import com.elves.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {


        return new ProductDTO(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado!")));
    }

    @Transactional(readOnly = true)
    public Page<ProductMinDTO> findAll(String name, Pageable pageable) {
        Page<Product> result = repository.searchByName(name, pageable);

        return result.map(ProductMinDTO::new);
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {

        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);

        return new ProductDTO(entity);

    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {

        try {
            Product entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);

            return new ProductDTO(entity);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Recurso não encontrado!");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        try {
            Product product =repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Recurso não encontrado!"));
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de Integridade referencial!");
        }

    }


    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());

        entity.getCategories().clear();
        dto.getCategories().forEach(x -> {
            Category cat = new Category();
            cat.setId(x.getId());
            entity.getCategories().add(cat);
        });
    }


}
