package com.stala.grzegorz.repository;


import com.stala.grzegorz.model.CartItem;
import com.stala.grzegorz.model.Order;
import com.stala.grzegorz.model.ShoppingCart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CartItemRepository extends CrudRepository<CartItem, Long> {
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	List<CartItem> findByOrder(Order order);
}
