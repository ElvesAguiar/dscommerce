package com.elves.dscommerce.services;

import com.elves.dscommerce.dto.ProductDTO;
import com.elves.dscommerce.entities.Product;
import com.elves.dscommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        return new ProductDTO(repository.findById(id).get());
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable){
        Page<Product> result = repository.findAll(pageable);

        return result.map(x -> new ProductDTO(x));
    }

    @Transactional
    public ProductDTO inset(ProductDTO dto){

        Product product = new Product();

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setImgUrl(dto.getImgUrl());
        product.setPrice(dto.getPrice());

        product = repository.save(product);

        return new ProductDTO(product);

    }
}
