package com.stala.grzegorz.repository;

import com.stala.grzegorz.model.Size;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends CrudRepository<Size,Long> {

    public Size findBySizeEUR(double sizeEUR);
}
