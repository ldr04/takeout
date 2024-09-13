package com.myvamp.vamp.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.myvamp.vamp.common.BaseContext;
import com.myvamp.vamp.common.R;
import com.myvamp.vamp.entity.ShoppingCart;
import com.myvamp.vamp.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {


    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 查看购物车
     * @param request
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(HttpServletRequest request){
        Long userId = (Long) request.getSession().getAttribute("user");
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,userId);
        lqw.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(lqw);
        return R.success(shoppingCartList);
    }

    /**
     * 向购物车添加物品
     * @param shoppingCart
     * @param request
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart,HttpServletRequest request){
        log.info(shoppingCart.toString());
        Long userId = (Long) request.getSession().getAttribute("user");
        shoppingCart.setUserId(userId);
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,shoppingCart.getUserId());

        Long dishId = shoppingCart.getDishId();
        if(dishId!=null){
//            添加的是套餐
            lqw.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else{
//            添加的是菜品
            lqw.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
//        执行查询
        ShoppingCart one = shoppingCartService.getOne(lqw);
        if(one!=null){
//            添加的物品已存在，数量加1
            one.setNumber(one.getNumber()+1);
            shoppingCartService.updateById(one);
        }else{
//            新增1个，需设置number值为1
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            one = shoppingCart;
        }

        return R.success(one);

        /*LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,userId);

        ShoppingCart shoppingCart1 = shoppingCartService.getOne(lqw);

        if(shoppingCart1==null) {
            shoppingCart.setUserId(userId);
            shoppingCartService.save(shoppingCart);
            return R.success("添加成功");
        }
        shoppingCart1.*/
    }

    /***
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){

        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        Long userId = BaseContext.getCurrentId();
        lqw.eq(ShoppingCart::getUserId,userId);
        shoppingCartService.remove(lqw);
        return R.success("删除成功");

    }

    /**
     * 删除购物车中的一件物品
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<List<ShoppingCart>> sub(@RequestBody ShoppingCart shoppingCart){
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,userId);
        Long dishId = shoppingCart.getDishId();
        if(dishId!=null){
            lqw.eq(ShoppingCart::getDishId,dishId);

        }else{
            lqw.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        ShoppingCart shoppingCart1 = shoppingCartService.getOne(lqw);
        if(shoppingCart1.getNumber()>1){
            shoppingCart1.setNumber(shoppingCart1.getNumber()-1);
            shoppingCartService.updateById(shoppingCart1);
        }else{
            shoppingCartService.removeById(shoppingCart1.getId());
        }
        LambdaQueryWrapper<ShoppingCart> lqw2 = new LambdaQueryWrapper<>();
        lqw2.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> list = shoppingCartService.list(lqw2);
        return R.success(list);
        /*list = list.stream().map((item)->{
            if(item.getNumber()>1){
                item.setNumber(item.getNumber()-1);
                shoppingCartService.updateById(item);

            }else{
                shoppingCartService.removeById(item.getId());
            }
            return item;
        }).collect(Collectors.toList());
        return R.success(list);*/
    }



}
