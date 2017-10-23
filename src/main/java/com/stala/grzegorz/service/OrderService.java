package com.stala.grzegorz.service;


import com.stala.grzegorz.model.*;

public interface OrderService {
	Order createOrder(ShoppingCart shoppingCart,
					  ShippingAddress shippingAddress,
					  String shippingMethod,
					  String paymentMethod,
					  User user);
	
	Order findOne(Long id);
}
