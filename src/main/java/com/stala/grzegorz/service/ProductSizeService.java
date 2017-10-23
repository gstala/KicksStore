package com.stala.grzegorz.service;

import com.stala.grzegorz.model.ProductSize;

import java.util.List;

public interface ProductSizeService {

    public ProductSize findOne(Long id);

    public ProductSize save(ProductSize productSize);

    public List<ProductSize> findAll();

}
