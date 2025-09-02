package com.bitemate.service.impl;

import com.bitemate.constant.MessageConstant;
import com.bitemate.constant.PasswordConstant;
import com.bitemate.constant.StatusConstant;
import com.bitemate.context.BaseContext;
import com.bitemate.dto.EmployeeDTO;
import com.bitemate.dto.EmployeeLoginDTO;
import com.bitemate.dto.EmployeePageQueryDTO;
import com.bitemate.entity.Employee;
import com.bitemate.exception.AccountLockedException;
import com.bitemate.exception.AccountNotFoundException;
import com.bitemate.exception.PasswordErrorException;
import com.bitemate.mapper.EmployeeMapper;
import com.bitemate.result.PageResult;
import com.bitemate.service.EmployeeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * Employee Login
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        //Get the username & password entered by user
        String username =employeeLoginDTO.getUsername();
        String password =employeeLoginDTO.getPassword();

        //Query the data in database by using the username
        Employee employee = employeeMapper.getEmployeeByUsername(username);

        //Handle exception
        if (employee == null) {
            //Account does not exist
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //Compare password
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if(!password.equals(employee.getPassword())) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if(employee.getStatus() == StatusConstant.DISABLE){
            //Account Locked
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        return employee;
    }

    /**
     * Add New Employee
     *
     * @param employeeDTO
     */
    public void addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        //Use BeanUtils to copy the data to employee (source, target)
        BeanUtils.copyProperties(employeeDTO, employee);

        //Set the status of the new account
        employee.setStatus(StatusConstant.ENABLE);

        //Set the default password in md5 Hash
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        //Set the Create and Update time & user
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        employee.setCreateUser(BaseContext.getCurrentId());
//        employee.setUpdateUser(BaseContext.getCurrentId());

        //Insert data to database
        employeeMapper.insertEmployee(employee);

    }

    /**
     * Page Query of Employee
     *
     * @param empPageQueryDTO
     * @return
     */
    public PageResult pageQueryEmployee(EmployeePageQueryDTO empPageQueryDTO) {
        PageHelper.startPage(empPageQueryDTO.getPage(),empPageQueryDTO.getPageSize());

        Page<Employee> empPage = employeeMapper.pageQueryEmployee(empPageQueryDTO);

        long total = empPage.getTotal();
        List<Employee> result = empPage.getResult();

        return new PageResult(total, result);
    }

    /**
     * Update Status of employee account
     *
     * @param status
     * @param id
     */
    public void updateStatusEmployee(Integer status, Long id) {
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();

        employeeMapper.updateEmployee(employee);
    }

    /**
     * Get employee by id
     *
     * @param id
     * @return
     */
    public Employee getEmployeeById(Long id) {
        Employee employee = employeeMapper.getEmployeeById(id);
        employee.setPassword("*****");
        return employee;
    }

    /**
     * Update employee data
     *
     * @param employeeDTO
     */
    public void updateEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        BeanUtils.copyProperties(employeeDTO, employee);
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.updateEmployee(employee);
    }
}
