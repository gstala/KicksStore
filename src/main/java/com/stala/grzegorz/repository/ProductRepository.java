package com.stala.grzegorz.repository;

import com.stala.grzegorz.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product,Long> {


    public List<Product> findByCategory(String category);

    public List<Product> findByProducer(String producer);

    public List<Product> findByNameContaining(String name);

    public List<Product> findFirst3ByActiveEquals(boolean active);

}
