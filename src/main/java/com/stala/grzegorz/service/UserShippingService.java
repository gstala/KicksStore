package com.stala.grzegorz.service;


import com.stala.grzegorz.model.UserShipping;

public interface UserShippingService {
	UserShipping findById(Long id);
	
	void removeById(Long id);
}
