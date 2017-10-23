package com.stala.grzegorz.service.impl;

import com.stala.grzegorz.model.ProductSize;
import com.stala.grzegorz.repository.ProductSizeRepository;
import com.stala.grzegorz.service.ProductSizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSizeServiceImpl implements ProductSizeService {

    @Autowired
    private ProductSizeRepository productSizeRepository;

    @Override
    public ProductSize findOne(Long id) {
        return productSizeRepository.findOne(id);
    }

    @Override
    public ProductSize save(ProductSize productSize) {
        return productSizeRepository.save(productSize);
    }

    @Override
    public List<ProductSize> findAll() {
        return (List<ProductSize>) productSizeRepository.findAll();
    }
}
