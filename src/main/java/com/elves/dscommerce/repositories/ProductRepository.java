package com.elves.dscommerce.repositories;

import com.elves.dscommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,  Long> {
}
