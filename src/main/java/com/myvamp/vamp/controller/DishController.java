package com.myvamp.vamp.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myvamp.vamp.common.R;
import com.myvamp.vamp.dto.DishDto;
import com.myvamp.vamp.entity.Category;
import com.myvamp.vamp.entity.Dish;
import com.myvamp.vamp.entity.DishFlavor;
import com.myvamp.vamp.service.CategoryService;
import com.myvamp.vamp.service.DishFlavorService;
import com.myvamp.vamp.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

@RestController
@ResponseBody
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 获取所有菜品
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> getAllByPage(Integer page,Integer pageSize,String name){

        Page<Dish> page1 = new Page<>(page,pageSize);
        Page<DishDto> pageDto = new Page<>();
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper();
        lqw.like(name!=null,Dish::getName,name);
        lqw.orderByDesc(Dish::getUpdateTime);
        dishService.page(page1,lqw);
        BeanUtils.copyProperties(page1,pageDto,"records");
        List<Dish> recordsDish = page1.getRecords();
        List<DishDto> recordsDto = recordsDish.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category!=null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        pageDto.setRecords(recordsDto);
        return R.success(pageDto);

    }

    /**
     * 添加菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> addDish(@RequestBody DishDto dishDto){

        log.info(dishDto.getFlavors().toString());
        dishService.saveWithFlavors(dishDto);
        return R.success("保存成功");
    }

    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id){
/*        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,id);
        Dish dishById = dishService.getById(id);
        DishDto dishDto = new DishDto();
        dishDto.setCategoryName(categoryService.getById(dishById.getCategoryId()).getName());
        dishDto.setFlavors(dishFlavorService.list(lqw));
        BeanUtils.copyProperties(dishById,dishDto);*/
        DishDto dishDto = dishService.getByIdWithFlavors(id);
        log.info(dishDto.toString());
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> saveUpdate(@RequestBody DishDto dishDto){
        dishService.updateWithFlavors(dishDto);
        return R.success("修改成功！");
    }

/*
    @Transactional
    @PostMapping("/status/0")
    public R<String> updateStatus(@RequestBody Long[] ids){
        */
/*LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper();

        lqw.eq(Dish::getId,id);*//*

        for (Long id : ids) {
            Dish dish = dishService.getById(id);
            Integer status = dish.getStatus();
            status = abs(status-1);
            dish.setStatus(status);
            dishService.updateById(dish);
        }
        return R.success("状态修改成功");
    }

*/

    /**
     * 添加套餐时回显菜品名称,以及添加菜品时的搜索
     * @param categoryId dish中的categoryId
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> getSetmealByCategoryId(Long categoryId,String name){
            LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
            lqw.eq(categoryId!=null,Dish::getCategoryId, categoryId);
            lqw.like(name!=null,Dish::getName,name);
            lqw.eq(Dish::getStatus,1);
            lqw.orderByDesc(Dish::getUpdateTime);
            List<Dish> list = dishService.list(lqw);
            List<DishDto> dishDtoList = new ArrayList<>();
            dishDtoList =list.stream().map((item)->{
                DishDto dishDto = new DishDto();
                BeanUtils.copyProperties(item,dishDto);
                LambdaQueryWrapper<DishFlavor> lqw2 = new LambdaQueryWrapper<>();
                lqw2.eq(DishFlavor::getDishId,dishDto.getId());
                List<DishFlavor> dishFlavors = dishFlavorService.list(lqw2);
                dishDto.setFlavors(dishFlavors);
                return dishDto;
            }).collect(Collectors.toList());
            return R.success(dishDtoList);
    }


}
