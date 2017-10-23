package com.stala.grzegorz.repository;


import com.stala.grzegorz.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {

}
