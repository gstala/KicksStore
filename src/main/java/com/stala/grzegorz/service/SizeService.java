package com.stala.grzegorz.service;

import com.stala.grzegorz.model.Size;

import java.util.List;

public interface SizeService {

    public Size findOne(Long id);

    public Size save(Size size);

    public List<Size> findAll();

    public Size findBySizeEUR(double sizeEUR);

}
