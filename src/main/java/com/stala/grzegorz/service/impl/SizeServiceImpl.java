package com.stala.grzegorz.service.impl;

import com.stala.grzegorz.model.Size;
import com.stala.grzegorz.repository.SizeRepository;
import com.stala.grzegorz.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeServiceImpl implements SizeService {


    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public Size findOne(Long id) {
        return sizeRepository.findOne(id);
    }

    @Override
    public Size save(Size size) {

        Size localSize = sizeRepository.findBySizeEUR(size.getSizeEUR());

        if (localSize == null)
            localSize = sizeRepository.save(size);

        return localSize;
    }

    @Override
    public List<Size> findAll() {
        return (List<Size>) sizeRepository.findAll();
    }

    @Override
    public Size findBySizeEUR(double sizeEUR) {
        return sizeRepository.findBySizeEUR(sizeEUR);
    }
}
