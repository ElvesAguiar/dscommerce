package com.elves.dscommerce.repositories;

import com.elves.dscommerce.entities.Order;
import com.elves.dscommerce.entities.OrderItem;
import com.elves.dscommerce.entities.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

}