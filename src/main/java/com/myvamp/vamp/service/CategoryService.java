package com.myvamp.vamp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myvamp.vamp.entity.Category;

public interface CategoryService extends IService<Category> {


    public void remove(Long id);

}
