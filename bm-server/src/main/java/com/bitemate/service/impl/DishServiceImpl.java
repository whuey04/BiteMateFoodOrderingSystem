package com.bitemate.service.impl;

import com.bitemate.constant.MessageConstant;
import com.bitemate.constant.StatusConstant;
import com.bitemate.dto.DishDTO;
import com.bitemate.dto.DishPageQueryDTO;
import com.bitemate.entity.Dish;
import com.bitemate.entity.DishFlavor;
import com.bitemate.exception.DeletionNotAllowedException;
import com.bitemate.mapper.DishFlavorMapper;
import com.bitemate.mapper.DishMapper;
import com.bitemate.mapper.SetmealDishMapper;
import com.bitemate.minio.service.FileStorageService;
import com.bitemate.result.PageResult;
import com.bitemate.result.Result;
import com.bitemate.service.DishService;
import com.bitemate.vo.DishVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private FileStorageService fileStorageService;


    /**
     * Page Query for all dish
     *
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult pageQueryDish(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQueryDish(dishPageQueryDTO);

        Long total = page.getTotal();
        List<DishVO> result = page.getResult();

        return new PageResult(total, result);
    }

    /**
     * Add new dish with flavor
     *
     * @param dishDTO
     */
    @Transactional
    public void addDishWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        dishMapper.insertDish(dish);

        Long dishId = dish.getId();

        List<DishFlavor> flavorList = dishDTO.getFlavors();
        if(flavorList != null && flavorList.size() > 0){
            flavorList.forEach(flavor -> {
                flavor.setDishId(dishId);
            });

            dishFlavorMapper.insertBatch(flavorList);
        }
    }

    /**
     * Get dish by id
     *
     * @param id
     * @return
     */
    public DishVO getDishById(Long id) {
        Dish dish = dishMapper.getDishById(id);

        List<DishFlavor> flavorList = dishFlavorMapper.getFlavorByDishId(id);

        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(flavorList);
        return dishVO;
    }

    /**
     * Update dish
     *
     * @param dishDTO
     */
    @Transactional
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        Dish tmp = dishMapper.getDishById(dishDTO.getId());
        if (dish.getImage() != null && tmp != null && !dish.getImage().equals(tmp.getImage())){
            fileStorageService.delete(tmp.getImage());
        }
        dishMapper.updateDish(dish);

        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        List<DishFlavor> flavorList = dishDTO.getFlavors();
        if(flavorList != null && flavorList.size() > 0){
            flavorList.forEach(flavor -> {
                flavor.setDishId(dishDTO.getId());
            });

            dishFlavorMapper.insertBatch(flavorList);
        }
    }

    /**
     * Set dish status
     *
     * @param status
     * @param id
     */
    public void updateDishStatus(Integer status, Long id) {
        Dish dish = Dish.builder()
                .status(status)
                .id(id)
                .build();
        dishMapper.updateDish(dish);

    }

    /**
     * Delete Dishes
     *
     * @param ids
     */
    @Transactional
    public void deleteDishByBatch(List<Long> ids) {
        List<Dish> dishes = dishMapper.getDishByIds(ids);
        if (dishes == null || dishes.isEmpty()) {
            return;
        }

        for (Dish dish : dishes) {
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIds != null && setmealIds.size() > 0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        for (Dish dish : dishes) {
            if (dish == null) continue;

            if (dish.getImage() != null){
                fileStorageService.delete(dish.getImage());
            }

            dishMapper.deleteById(dish.getId());
            dishFlavorMapper.deleteByDishId(dish.getId());
        }
    }

    /**
     * Search for dishes by category id
     *
     * @param categoryId
     * @return
     */
    public List<Dish> listDish(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();

        return dishMapper.list(dish);
    }

    /**
     * Get dish list by category id
     *
     * @param categoryId
     * @return
     */
    @Transactional
    public List<DishVO> getDishlistWithFlavor(Long categoryId) {
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);

        List<Dish> dishList = dishMapper.list(dish);
        List<DishVO> dishVOList = new ArrayList<>();
        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            List<DishFlavor> flavorList = dishFlavorMapper.getFlavorByDishId(d.getId());

            dishVO.setFlavors(flavorList);
            dishVOList.add(dishVO);
        }
        return dishVOList;
    }


}
