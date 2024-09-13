package com.myvamp.vamp.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myvamp.vamp.common.CustomException;
import com.myvamp.vamp.dto.SetmealDto;
import com.myvamp.vamp.entity.Setmeal;
import com.myvamp.vamp.entity.SetmealDish;
import com.myvamp.vamp.mapper.SetmealMapper;
import com.myvamp.vamp.service.SetmealDishService;
import com.myvamp.vamp.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 保存带菜品的套餐信息
     * @param setmealDto
     */
    @Transactional
    @Override
    public void saveWithDishes(SetmealDto setmealDto) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto,setmeal);
        this.save(setmeal);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item)->{
            item.setSetmealId(setmeal.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐及关联菜品
      * @param ids 套餐id数组
     */

    @Transactional
    @Override
    public void removeWithDishes(List<Long> ids) {

        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.in(Setmeal::getId,ids);
        lqw.eq(Setmeal::getStatus,1);
        int count = this.count(lqw);
        if(count>0){
            throw new CustomException("所选内容正在售卖中，无法删除");
        }
        this.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> lqw2 = new LambdaQueryWrapper<>();
        lqw2.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lqw2);

    }


}
