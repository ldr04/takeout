package com.myvamp.vamp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myvamp.vamp.entity.Orders;

public interface OrdersService extends IService<Orders> {

    public void submit(Orders orders);

}
