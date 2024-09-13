package com.myvamp.vamp.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myvamp.vamp.common.R;
import com.myvamp.vamp.dto.DishDto;
import com.myvamp.vamp.dto.SetmealDto;
import com.myvamp.vamp.entity.Dish;
import com.myvamp.vamp.entity.DishFlavor;
import com.myvamp.vamp.entity.Setmeal;
import com.myvamp.vamp.entity.SetmealDish;
import com.myvamp.vamp.service.CategoryService;
import com.myvamp.vamp.service.SetmealDishService;
import com.myvamp.vamp.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@ResponseBody
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 带搜索的分页查询
     * @param page  当前页码
     * @param pageSize  每页条数
     * @param name  搜索名称
     * @return
     */

    @GetMapping("/page")
    public R<Page> getAllByPage(Integer page,Integer pageSize,String name){
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.like(name!=null,Setmeal::getName,name);
        setmealService.page(setmealPage,lqw);
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> recordsDto = setmealDtoPage.getRecords();
        recordsDto = records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            setmealDto.setCategoryName(categoryService.getById(item.getCategoryId()).getName());
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(recordsDto);
        return R.success(setmealDtoPage);

    }


    /**
     * 增加菜品
     * @param setmealDto 为setmeal对象+里面菜品信息
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDishes(setmealDto);
        return R.success("保存成功");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.removeWithDishes(ids);
        return R.success("删除成功");
    }
    @GetMapping("/list")
    public R<List<SetmealDto>> getSetmealByCategoryId(Long categoryId, String name){
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.eq(categoryId!=null,Setmeal::getCategoryId, categoryId);
        lqw.like(name!=null,Setmeal::getName,name);
        lqw.eq(Setmeal::getStatus,1);
        lqw.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(lqw);
        List<SetmealDto> setmealDtoList = new ArrayList<>();
        setmealDtoList =list.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            LambdaQueryWrapper<SetmealDish> lqw2 = new LambdaQueryWrapper<>();
            lqw2.eq(SetmealDish::getDishId,setmealDto.getId());
            List<SetmealDish> dishFlavors = setmealDishService.list(lqw2);
            setmealDto.setSetmealDishes(dishFlavors);
            return setmealDto;
        }).collect(Collectors.toList());
        return R.success(setmealDtoList);
    }

}
