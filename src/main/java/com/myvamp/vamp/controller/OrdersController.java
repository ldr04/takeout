package com.myvamp.vamp.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.myvamp.vamp.common.BaseContext;
import com.myvamp.vamp.common.R;
import com.myvamp.vamp.entity.Orders;
import com.myvamp.vamp.entity.ShoppingCart;
import com.myvamp.vamp.service.OrdersService;
import com.myvamp.vamp.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@ResponseBody
@RequestMapping("/order")
@Slf4j
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ShoppingCartService shoppingCartService;



    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return  R.success("操作成功");
    }





}
