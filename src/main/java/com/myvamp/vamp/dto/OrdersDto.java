package com.myvamp.vamp.dto;


import com.myvamp.vamp.entity.OrderDetail;
import com.myvamp.vamp.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
