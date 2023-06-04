package com.elves.dscommerce.services;

import com.elves.dscommerce.dto.OrderDTO;
import com.elves.dscommerce.repositories.OrderRepository;
import com.elves.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id){

        return new OrderDTO(repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Recurso não encontrado!")));
    }



}
