package com.myvamp.vamp.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myvamp.vamp.entity.OrderDetail;
import com.myvamp.vamp.mapper.OrderDetailMapper;
import com.myvamp.vamp.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper,OrderDetail> implements OrderDetailService {
}