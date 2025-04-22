package com.example.liquorstore.service;

import com.example.liquorstore.dto.LiquorDto;
import com.example.liquorstore.entity.Liquor;
import com.example.liquorstore.repository.LiquorRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LiquorService {
    @Autowired
    LiquorRepository liquorRepository;

    public Liquor addLiquor(LiquorDto liquorDto) {
        Liquor liquor = new Liquor();
        BeanUtils.copyProperties(liquorDto, liquor);
        return liquorRepository.save(liquor);
    }

    public void deleteLiquorById(long id) {
        liquorRepository.deleteById(id);
    }

    public List<Liquor> getAllLiquors() {
        return liquorRepository.findAll();
    }
}
