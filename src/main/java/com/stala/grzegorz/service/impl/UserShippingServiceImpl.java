package com.stala.grzegorz.service.impl;

import com.stala.grzegorz.model.UserShipping;
import com.stala.grzegorz.repository.UserShippingRepository;
import com.stala.grzegorz.service.UserShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserShippingServiceImpl implements UserShippingService {

    @Autowired
    private UserShippingRepository userShippingRepository;


    public UserShipping findById(Long id) {
        return userShippingRepository.findOne(id);
    }

    public void removeById(Long id) {
        userShippingRepository.delete(id);
    }

}
