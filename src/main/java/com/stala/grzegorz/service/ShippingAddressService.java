package com.stala.grzegorz.service;


import com.stala.grzegorz.model.ShippingAddress;
import com.stala.grzegorz.model.UserShipping;

public interface ShippingAddressService {
	ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress);
}
