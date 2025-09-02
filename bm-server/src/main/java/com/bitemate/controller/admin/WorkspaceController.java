package com.bitemate.controller.admin;

import com.bitemate.result.Result;
import com.bitemate.service.WorkspaceService;
import com.bitemate.vo.BusinessDataVO;
import com.bitemate.vo.DishOverViewVO;
import com.bitemate.vo.OrderOverViewVO;
import com.bitemate.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/admin/workspace")
@Slf4j
@Api(tags = "Admin - Workspace Controller")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    /**
     * Get today business data
     * @return
     */
    @GetMapping("/businessData")
    @ApiOperation(value = "Get Today Business Data")
    public Result<BusinessDataVO> getTodayBusinessData() {
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);

        BusinessDataVO businessData = workspaceService.getTodayBusinessData(begin, end);
        return Result.success(businessData);
    }

    /**
     * Get overview of orders
     * @return
     */
    @GetMapping("/overviewOrders")
    @ApiOperation(value = "Get Overview od Orders")
    public Result<OrderOverViewVO> orderOverview(){
        return Result.success(workspaceService.getOrderOverview());
    }

    /**
     * Get overview of dishes
     * @return
     */
    @GetMapping("/overviewDishes")
    @ApiOperation(value = "Get Overview of Dishes")
    public Result<DishOverViewVO> dishOverview(){
        return Result.success(workspaceService.getDishOverview());
    }

    /**
     * Get overview of set meals
     * @return
     */
    @GetMapping("/overviewSetmeals")
    @ApiOperation(value = "Get Overview of Set Meals")
    public Result<SetmealOverViewVO> setmealOverview(){
        return Result.success(workspaceService.getSetmealOverview());
    }
}
