package com.myvamp.vamp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myvamp.vamp.dto.SetmealDto;
import com.myvamp.vamp.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    public void saveWithDishes(SetmealDto setmealDto);


    public void removeWithDishes(List<Long> ids);
}
