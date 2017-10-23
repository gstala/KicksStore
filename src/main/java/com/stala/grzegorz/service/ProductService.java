package com.stala.grzegorz.service;

import com.stala.grzegorz.model.Product;

import java.util.List;

public interface ProductService {

    public Product findOne(Long id);

    public Product save(Product product);

    public List<Product> findAll();

    public void removeOne(Long id);

    public List<Product> findByCategory(String category);

    public List<Product> blurrySearch(String name);

    public List<Product> findByProducer(String producer);

    public List<Product> findFirst3ByActiveEquals(boolean active);

    public void setProductActualInStockNumber(Product product);

}
