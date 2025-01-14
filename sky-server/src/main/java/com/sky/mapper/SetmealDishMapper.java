package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品id查询套餐id
     * @param dishIds
     * @return
     */
    List<Long> getSetmealIdByDishIds(List<Long> dishIds);

    /**
     * 批量插入套餐菜品
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 删除套餐菜品根据套餐id
     * @param id
     */
    @Delete("delete from setmeal_dish where id = #{id}")
    void deleteBySetmealId(Long id);

    /**
     * 查找套餐菜品根据id
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where id = #{id}")
    List<SetmealDish> getBySetmealId(Long id);
}
