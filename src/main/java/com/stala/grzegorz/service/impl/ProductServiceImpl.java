package com.stala.grzegorz.service.impl;

import com.stala.grzegorz.model.Product;
import com.stala.grzegorz.model.ProductSize;
import com.stala.grzegorz.repository.ProductRepository;
import com.stala.grzegorz.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product findOne(Long id) {
        return productRepository.findOne(id);
    }

    @Override
    public Product save(Product product) {
        setProductActualInStockNumber(product);
        return productRepository.save(product);
    }

    @Override
    public List<Product> findAll() {
        return (List<Product>) productRepository.findAll();
    }

    @Override
    public void removeOne(Long id) {
        productRepository.delete(id);
    }

    @Override
    public List<Product> findByCategory(String category) {
        List<Product> productList = productRepository.findByCategory(category);

        List<Product> activeProductList = new ArrayList<>();

        for (Product product : productList) {
            if (product.isActive())
                activeProductList.add(product);
        }
        return activeProductList;
    }

    @Override
    public List<Product> blurrySearch(String name) {
        List<Product> productList = productRepository.findByNameContaining(name);
        List<Product> activeProductList = new ArrayList<>();

        for (Product product : productList) {
            if (product.isActive()) {
                activeProductList.add(product);
            }
        }

        return activeProductList;
    }

    @Override
    public List<Product> findByProducer(String producer) {
        return productRepository.findByProducer(producer);
    }

    @Override
    public List<Product> findFirst3ByActiveEquals(boolean active) {
        return productRepository.findFirst3ByActiveEquals(active);
    }

    @Override
    public void setProductActualInStockNumber(Product product) {
        int temp = 0;
        for (ProductSize productSize : product.getProductSizeList()) {
            temp += productSize.getQuantity();
        }
        product.setInStockNumber(temp);
    }


}
