package com.myvamp.vamp.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myvamp.vamp.common.R;
import com.myvamp.vamp.entity.Category;
import com.myvamp.vamp.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@ResponseBody
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 查询所有分类
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> getCategoryPage(Integer page,Integer pageSize){
        Page categoryPage = new Page(page,pageSize);
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.orderByAsc(Category::getSort);
        Page page1 = categoryService.page(categoryPage, lqw);
        return R.success(page1);
    }

    /**
     * 增加
     * @param category
     * @return
     */
    @PostMapping
    public R<String> addCategory(@RequestBody Category category){
        categoryService.save(category);
        return R.success("保存成功！");
    }

    /**
     * 修改
     * @param category
     * @return
     */
    @PutMapping
    public R<String> updateById(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功!");
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> deleteById(Long id){
        categoryService.remove(id);
        return R.success("删除成功!");
    }

    /**
     * 查询所有菜品或套餐分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> getCategoryByType(Integer type){

        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(type!=null,Category::getType,type);
        List<Category> categoryList= categoryService.list(lqw);
        log.info(categoryList.toString());

        return R.success(categoryList);

    }



}
