package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 套餐业务实现
 */
@Service
@Slf4j
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }

    /**
     * 新增套餐
     * @param setmealdto
     */
    @Override
    @Transactional
    public void save(SetmealDTO setmealdto) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealdto, setmeal);

        //向表中插入套餐数据
        setmealMapper.insert(setmeal);

        List<SetmealDish> setmealDishes = setmealdto.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmeal.getId());
        });

        setmealDishMapper.insertBatch(setmealDishes);
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        ids.forEach(id->{
            Setmeal setmeal = setmealMapper.getById(id);
            if(setmeal.getStatus().equals(MessageConstant.SETMEAL_ON_SALE)){
                //起售中的套餐不能删除
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        ids.forEach(id->{
            setmealMapper.deleteById(id);
            setmealDishMapper.deleteBySetmealId(id);
        });
    }

    /**
     * 根据id查询套餐和它的dish
     * @param id
     * @return
     */
    @Override
    public SetmealVO getByIdWithDish(Long id) {
        //查找setmeal并拷贝进setmealVO
        Setmeal setmeal = setmealMapper.getById(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);

        //查找套餐菜品
        List<SetmealDish> smdls = setmealDishMapper.getBySetmealId(id);
        setmealVO.setSetmealDishes(smdls);

        return setmealVO;
    }

    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        //转换为setmeal
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        //更新表中的setmeal
        setmealMapper.update(setmeal);

        //获得setmealID
        Long id = setmealDTO.getId();

        //获得套餐关联菜品
        List<SetmealDish> bySetmealId = setmealDTO.getSetmealDishes();

        //删除表中关联菜品
        setmealDishMapper.deleteBySetmealId(id);

        //重新设关联菜品的套餐id
        bySetmealId.forEach(setmealDish -> {
            setmealDish.setSetmealId(id);
        });

        //插入关联菜品表中
        setmealDishMapper.insertBatch(bySetmealId);
    }

    /**
     * 修改套餐状态
     * @param status
     * @param id
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        if(status == StatusConstant.ENABLE){
            List<Dish> dishls = dishMapper.getBySetmealId(id);
            dishls.forEach(dish -> {
                if(dish.getStatus() == StatusConstant.DISABLE){
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            });
        }
        Setmeal setmeal = Setmeal.builder()
                                .id(id)
                                .status(status)
                                .build();
        setmealMapper.update(setmeal);
    }
}
