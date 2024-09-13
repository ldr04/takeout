package com.myvamp.vamp.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myvamp.vamp.entity.DishFlavor;
import com.myvamp.vamp.mapper.DishFlavorMapper;
import com.myvamp.vamp.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
