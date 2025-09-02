package com.bitemate.service;

import com.bitemate.dto.EmployeeDTO;
import com.bitemate.dto.EmployeeLoginDTO;
import com.bitemate.dto.EmployeePageQueryDTO;
import com.bitemate.entity.Employee;
import com.bitemate.result.PageResult;

public interface EmployeeService {

    /**
     * Employee Login
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * Add New Employee
     * @param employeeDTO
     */
    void addEmployee(EmployeeDTO employeeDTO);

    /**
     * Page Query of Employee
     * @param empPageQueryDTO
     * @return
     */
    PageResult pageQueryEmployee(EmployeePageQueryDTO empPageQueryDTO);

    /**
     * Update Status of employee account
     * @param status
     * @param id
     */
    void updateStatusEmployee(Integer status, Long id);

    /**
     * Get employee by id
     * @param id
     * @return
     */
    Employee getEmployeeById(Long id);

    /**
     * Update employee data
     * @param employeeDTO
     */
    void updateEmployee(EmployeeDTO employeeDTO);
}
