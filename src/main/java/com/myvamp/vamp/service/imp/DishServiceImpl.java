package com.myvamp.vamp.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myvamp.vamp.dto.DishDto;
import com.myvamp.vamp.entity.Dish;
import com.myvamp.vamp.entity.DishFlavor;
import com.myvamp.vamp.mapper.DishMapper;
import com.myvamp.vamp.service.DishFlavorService;
import com.myvamp.vamp.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;


    /**
     * 保存菜品和菜品口味表
     * @param dishDto
     */
    @Transactional
    @Override
    public void saveWithFlavors(DishDto dishDto) {
        this.save(dishDto);
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 通过菜品id查询返回dto对象
     * @param id 菜品id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavors(Long id) {
        DishDto dishDto = new DishDto();
        Dish dishById = this.getById(id);
        BeanUtils.copyProperties(dishById,dishDto);
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,id);
        List<DishFlavor> list = dishFlavorService.list(lqw);
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Transactional
    @Override
    public void updateWithFlavors(DishDto dishDto) {
        this.updateById(dishDto);
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,dishDto.getId());

        List<DishFlavor> dishFlavors = dishDto.getFlavors();
        dishFlavors = dishFlavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        if(dishFlavorService.remove(lqw)){
            dishFlavorService.saveBatch(dishFlavors);
        }

    }
}
