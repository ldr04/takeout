package com.myvamp.vamp.dto;


import com.myvamp.vamp.entity.Dish;
import com.myvamp.vamp.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;


/**
 * DataTransferObject用于封装传输的对象
 */

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
