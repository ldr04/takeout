package com.myvamp.vamp.dto;


import com.myvamp.vamp.entity.Setmeal;
import com.myvamp.vamp.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
