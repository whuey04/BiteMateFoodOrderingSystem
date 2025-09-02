package com.bitemate.mapper;

import com.bitemate.annotation.AutoFill;
import com.bitemate.dto.EmployeePageQueryDTO;
import com.bitemate.entity.Employee;
import com.bitemate.enumeration.OperationType;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * Get employee by using the username
     *
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getEmployeeByUsername(String username);

    /**
     * Insert Employee data
     *
     * @param employee
     */
    @Insert("insert into employee(name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user,status)" +
            "values "+
            "(#{name}, #{username}, #{password}, #{phone}, #{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser},#{status})"
    )
    @AutoFill(value = OperationType.INSERT)
    void insertEmployee(Employee employee);

    /**
     * Page Query
     *
     * @param empPageQueryDTO
     * @return
     */
    Page<Employee> pageQueryEmployee(EmployeePageQueryDTO empPageQueryDTO);

    /**
     * Update Employee
     *
     * @param employee
     */
    @AutoFill(value = OperationType.UPDATE)
    void updateEmployee(Employee employee);

    /**
     * Get Employee By using ID
     *
     * @param id
     * @return
     */
    @Select("select * from employee where id = #{id}")
    Employee getEmployeeById(Long id);
}
