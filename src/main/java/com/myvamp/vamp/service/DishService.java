package com.myvamp.vamp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myvamp.vamp.dto.DishDto;
import com.myvamp.vamp.entity.Dish;

public interface DishService extends IService<Dish> {

    public void saveWithFlavors(DishDto dishDto);

    public DishDto getByIdWithFlavors(Long id);

    public void updateWithFlavors(DishDto dishDto);

}
