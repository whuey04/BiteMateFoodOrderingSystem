package com.bitemate.mapper;

import com.bitemate.dto.GoodsSalesDTO;
import com.bitemate.dto.OrdersPageQueryDTO;
import com.bitemate.entity.Orders;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * Insert order
     * @param order
     */
    void insertOrder(Orders order);

    /**
     * Get order by using orderNumber and user id
     * @param orderNumber
     * @param userId
     * @return
     */
    @Select("select * from orders where number=#{orderNumber} and user_id=#{userId}")
    Orders getOrder(String orderNumber, Long userId);

    /**
     * Update Order
     * @param orders
     */
    void updateOrder(Orders orders);

    /**
     * Paginated query with filtering conditions and sorts by order time.
     * @param pageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO pageQueryDTO);

    /**
     * Get order by id
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    Orders getOrderById(Long id);

    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);

    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getOrderByStatusAndOrdertime(Integer status, LocalDateTime orderTime);

    /**
     * Sum the statistics
     * @param map
     * @return
     */
    Double sumStatistics(Map map);

    /**
     * Count the total and valid order
     * @param map
     * @return
     */
    Integer countOrderByMap(Map map);

    /**
     * Query sales top 10
     * @param begin
     * @param end
     * @return
     */
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin, LocalDateTime end);
}
