package com.bitemate.service.impl;

import com.bitemate.constant.MessageConstant;
import com.bitemate.constant.StatusConstant;
import com.bitemate.dto.SetmealDTO;
import com.bitemate.dto.SetmealPageQueryDTO;
import com.bitemate.entity.Dish;
import com.bitemate.entity.Setmeal;
import com.bitemate.entity.SetmealDish;
import com.bitemate.exception.DeletionNotAllowedException;
import com.bitemate.exception.SetmealEnableFailedException;
import com.bitemate.mapper.DishMapper;
import com.bitemate.mapper.SetmealDishMapper;
import com.bitemate.mapper.SetmealMapper;
import com.bitemate.minio.service.FileStorageService;
import com.bitemate.result.PageResult;
import com.bitemate.service.SetmealService;
import com.bitemate.vo.DishItemVO;
import com.bitemate.vo.SetmealVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Page Query for set meal
     *
     * @param dto
     * @return
     */
    public PageResult pageQuerySetmeal(SetmealPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());

        Page<SetmealVO> page = setmealMapper.pageQuerySetmeal(dto);

        Long total = page.getTotal();
        List<SetmealVO> result = page.getResult();
        return new PageResult(total, result);
    }

    /**
     * Add set meal
     *
     * @param setmealDTO
     */
    @Transactional
    public void addSetmealWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        setmealMapper.insertSetmeal(setmeal);

        Long setmealId = setmeal.getId();

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });

        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * Get set meal by id
     *
     * @param id
     * @return
     */
    public SetmealVO getSetmealByIdWithDish(Long id) {
        Setmeal setmeal = setmealMapper.getSetmealById(id);
        List<SetmealDish> setmealDishes = setmealDishMapper.getDishesBySetmealId(id);

        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    /**
     * Update set meal
     *
     * @param setmealDTO
     */
    @Transactional
    public void updateSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        Setmeal tmp = setmealMapper.getSetmealById(setmealDTO.getId());
        if(setmeal.getImage() != null && tmp != null && !setmeal.getImage().equals(tmp.getImage())){
            fileStorageService.delete(tmp.getImage());
        }

        setmealMapper.updateSetmeal(setmeal);

        Long setmealId = setmeal.getId();

        setmealDishMapper.deleteBySetmealId(setmealId);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * Update set meal status
     *
     * @param status
     * @param id
     */
    public void updateSetmealStatus(Integer status, Long id) {
        if(status == StatusConstant.ENABLE){
            List<Dish> dishList = dishMapper.getDishBySetmealId(id);
            if (dishList != null && dishList.size() > 0) {
                dishList.forEach(dish -> {
                    if(StatusConstant.DISABLE == dish.getStatus()){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }

        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();

        setmealMapper.updateSetmeal(setmeal);
    }

    /**
     * Delete set meal
     *
     * @param ids
     */
    @Transactional
    public void deleteSetmealbyBatch(List<Long> ids) {
        List<Setmeal> setmeals = setmealMapper.getSetmealByIds(ids);
        if (setmeals == null || setmeals.size() == 0) {
            return;
        }

        for (Setmeal setmeal : setmeals) {
            if (StatusConstant.ENABLE == setmeal.getStatus()) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        setmeals.forEach(setmeal -> {
            if (setmeal.getImage() != null){
                fileStorageService.delete(setmeal.getImage());
            }
            setmealMapper.deleteById(setmeal.getId());
            setmealDishMapper.deleteBySetmealId(setmeal.getId());
        });
    }

    //User

    /**
     * Get set meal list by category id
     *
     * @param categoryId
     * @return
     */
    public List<Setmeal> getSetmealList(Long categoryId) {
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(StatusConstant.ENABLE);

        List<Setmeal> setmealList = setmealMapper.listSetmeal(setmeal);

        return setmealList;
    }

    /**
     * Get the dish list included in the set meal
     *
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
