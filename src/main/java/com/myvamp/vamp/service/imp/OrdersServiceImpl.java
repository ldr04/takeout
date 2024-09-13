package com.myvamp.vamp.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myvamp.vamp.common.BaseContext;
import com.myvamp.vamp.common.CustomException;
import com.myvamp.vamp.dto.OrdersDto;
import com.myvamp.vamp.entity.*;
import com.myvamp.vamp.mapper.OrdersMapper;
import com.myvamp.vamp.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;


    @Override
    public void submit(Orders orders) {

        //获取当前用户id和购物车信息
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> lqw1 = new LambdaQueryWrapper<>();
        lqw1.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(lqw1);
        if(shoppingCartList==null || shoppingCartList.size()==0){
            throw new CustomException("当前购物车为空无法下单");
        }
        long orderNumber = IdWorker.getId();//订单号
//        获取地址信息
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if(addressBook==null){
            throw new CustomException("当前地址为空");
        }
//        计算总金额，保存订单明细
        AtomicInteger amount = new AtomicInteger(0);//原子操作保证结果不出错
        List<OrderDetail> orderDetailList = shoppingCartList.stream().map((item)->{
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setNumber(item.getNumber());
            orderDetail.setAmount(item.getAmount());
            orderDetail.setImage(item.getImage());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setName(item.getName());
            orderDetail.setOrderId(orderNumber);
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        User user = userService.getById(userId);

        orders.setCheckoutTime(LocalDateTime.now());
        orders.setNumber(String.valueOf(orderNumber));
        orders.setId(orderNumber);
        orders.setOrderTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(BigDecimal.valueOf(amount.get()));
        orders.setUserId(userId);
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "":addressBook.getProvinceName())
                +(addressBook.getCityName()==null ? "" :addressBook.getCityName())
                +(addressBook.getDistrictName() == null ? "" :addressBook.getDistrictName())
                +(addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        this.save(orders);
        orderDetailService.saveBatch(orderDetailList);
        shoppingCartService.remove(lqw1);



    }
}
