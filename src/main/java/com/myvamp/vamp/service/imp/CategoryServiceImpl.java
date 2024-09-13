package com.myvamp.vamp.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myvamp.vamp.common.CustomException;
import com.myvamp.vamp.entity.Category;
import com.myvamp.vamp.entity.Dish;
import com.myvamp.vamp.entity.Setmeal;
import com.myvamp.vamp.mapper.CategoryMapper;
import com.myvamp.vamp.service.CategoryService;
import com.myvamp.vamp.service.DishService;
import com.myvamp.vamp.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 通过id删除分类，删除之前查询是否已经关联相应的菜品或者套餐
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLqw = new LambdaQueryWrapper<>();
        dishLqw.eq(Dish::getCategoryId,id);
        int countDish = dishService.count(dishLqw);
        //count>0 说明已经关联了菜品，不能删除，抛出一个业务异常
        if(countDish>0){
            throw new CustomException("已关联菜品，无法操作");
        }
        LambdaQueryWrapper<Setmeal> setmealLqw = new LambdaQueryWrapper<>();
        setmealLqw.eq(Setmeal::getCategoryId,id);
        int countSetmeal = setmealService.count(setmealLqw);
        if (countSetmeal>0){
            throw new CustomException("已关联套餐，无法操作");
        }

        //都没有关联可执行删除
        super.removeById(id);


    }
}
