package com.myvamp.vamp.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myvamp.vamp.entity.ShoppingCart;
import com.myvamp.vamp.mapper.ShoppingCartMapper;
import com.myvamp.vamp.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
