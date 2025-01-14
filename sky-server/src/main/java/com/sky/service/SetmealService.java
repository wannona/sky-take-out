package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import java.util.List;

public interface SetmealService {

//    /**
//     * 条件查询
//     * @param setmeal
//     * @return
//     */
//    List<Setmeal> list(Setmeal setmeal);
//
//    /**
//     * 根据id查询菜品选项
//     * @param id
//     * @return
//     */
//    List<DishItemVO> getDishItemById(Long id);

    /**
     * 新增套餐
     * @param setmealdto
     */
    void save(SetmealDTO setmealdto);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 套餐删除
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询套餐和它的dish
     * @param id
     * @return
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * 修改套餐
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 修改套餐状态
     * @param status
     * @param id
     */
    void updateStatus(Integer status, Long id);

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
}
