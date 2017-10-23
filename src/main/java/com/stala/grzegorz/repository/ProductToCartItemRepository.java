package com.stala.grzegorz.repository;


import com.stala.grzegorz.model.ProductToCartItem;
import com.stala.grzegorz.model.CartItem;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface ProductToCartItemRepository extends CrudRepository<ProductToCartItem, Long> {
	void deleteByCartItem(CartItem cartItem);
}
