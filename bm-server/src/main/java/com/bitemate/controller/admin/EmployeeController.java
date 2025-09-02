package com.bitemate.controller.admin;

import com.bitemate.constant.JwtClaimsConstant;
import com.bitemate.context.BaseContext;
import com.bitemate.dto.EmployeeDTO;
import com.bitemate.dto.EmployeeLoginDTO;
import com.bitemate.dto.EmployeePageQueryDTO;
import com.bitemate.entity.Employee;
import com.bitemate.properties.JwtProperties;
import com.bitemate.result.PageResult;
import com.bitemate.result.Result;
import com.bitemate.service.EmployeeService;
import com.bitemate.utils.JwtUtil;
import com.bitemate.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "Admin - Employee Controller")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * Employee Login
     *
     * @param empLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "Employee Login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO empLoginDTO) {
        log.info("Employee Login:{{}}", empLoginDTO);

        Employee employee = employeeService.login(empLoginDTO);

        //Generate JWT Token after login successful
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.generateJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO empLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(empLoginVO);
    }

    /**
     * Employee Logout
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "Employee Logout")
    public Result<String> logout() {
        BaseContext.removeCurrentId();
        return Result.success();
    }

    /**
     * Create new Employee
     *
     * @param employeeDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "Add New Employee")
    public Result addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("Add New Employee:{}", employeeDTO);
        employeeService.addEmployee(employeeDTO);
        return Result.success();
    }

    /**
     * Page query for all employee data
     *
     * @param empPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "Page Query for Employee Info")
    public Result<PageResult> queryEmployeeInPage(EmployeePageQueryDTO empPageQueryDTO) {
        log.info("Employee Page Query:{}", empPageQueryDTO);
        PageResult pageResult = employeeService.pageQueryEmployee(empPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Update status of Employee account
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "Update Status of Employee Account")
    public Result updateStatusEmployee(@PathVariable("status") Integer status, Long id) {
        log.info("Update Status = {} Employee Id:{}", status, id);
        employeeService.updateStatusEmployee(status, id);
        return Result.success();
    }

    /**
     * Get employee data by ID
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Get Employee")
    public Result<Employee> getEmployeeById(@PathVariable("id") Long id) {
        log.info("Get Employee by Id:{}", id);
        Employee employee = employeeService.getEmployeeById(id);
        return Result.success(employee);
    }

    /**
     * Update employee data
     *
     * @param employeeDTO
     * @return
     */
    @PutMapping
    @ApiOperation(value = "Update Employee Data")
    public Result updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("Update Employee:{}", employeeDTO);
        employeeService.updateEmployee(employeeDTO);
        return Result.success();
    }
}
