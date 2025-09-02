package com.bitemate.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bitemate.constant.MessageConstant;
import com.bitemate.context.BaseContext;
import com.bitemate.dto.*;
import com.bitemate.entity.*;
import com.bitemate.exception.AddressBookBusinessException;
import com.bitemate.exception.OrderBusinessException;
import com.bitemate.exception.ShoppingCartBusinessException;
import com.bitemate.mapper.*;
import com.bitemate.result.PageResult;
import com.bitemate.service.OrderService;
import com.bitemate.vo.OrderPaymentVO;
import com.bitemate.vo.OrderStatisticsVO;
import com.bitemate.vo.OrderSubmitVO;
import com.bitemate.vo.OrderVO;
import com.bitemate.websocket.WebSocketServer;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * Place order
     *
     * @param ordersSubmitDTO
     * @return
     */
    @Transactional
    public OrderSubmitVO placeOrder(OrdersSubmitDTO ordersSubmitDTO) {
        AddressBook addressBook = addressMapper.getAddressById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);

        //Check current user's shopping cart data
        List<ShoppingCart> cartList = shoppingCartMapper.getList(shoppingCart);
        if (cartList == null || cartList.size() == 0) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //Create order data
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, order);
        order.setPhone(addressBook.getPhone());
        order.setAddress(addressBook.getDetail());
        order.setConsignee(addressBook.getConsignee());
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setUserId(userId);
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setPayStatus(Orders.UN_PAID);
        order.setOrderTime(LocalDateTime.now());

        orderMapper.insertOrder(order);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : cartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(order.getId());
            orderDetailList.add(orderDetail);
        }

        orderDetailMapper.insertBatch(orderDetailList);

        shoppingCartMapper.deleteByUserId(userId);

        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(order.getId())
                .orderNumber(order.getNumber())
                .orderAmount(order.getAmount())
                .orderTime(order.getOrderTime())
                .build();

        return orderSubmitVO;
    }

    /**
     * Order payment
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) {
        //Get current user id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //Simulated payment
        JSONObject jsonObject = new JSONObject();
        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("This order has been paid for.");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * Purchase completed, update order status
     *
     * @param orderNumber
     */
    public void paySuccess(String orderNumber) {
        Long userId = BaseContext.getCurrentId();
        Orders ordersDB = orderMapper.getOrder(orderNumber,userId);

        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.updateOrder(orders);


        ///
        Map map = new HashMap();
        map.put("type",1);
        map.put("orderId", orders.getId());
        map.put("content", "OrderNo：" + orderNumber);
        webSocketServer.sendToAllClient(JSON.toJSONString(map));
    }

    /**
     * Get history orders list
     *
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    public PageResult pageQueryHistoryOrders(int page, int pageSize, Integer status) {
        PageHelper.startPage(page, pageSize);

        OrdersPageQueryDTO pageQueryDTO = new OrdersPageQueryDTO();
        pageQueryDTO.setUserId(BaseContext.getCurrentId());
        pageQueryDTO.setStatus(status);

        Page<Orders> ordersPage = orderMapper.pageQuery(pageQueryDTO);

        List<OrderVO> orderVOList = new ArrayList();

        if(ordersPage != null && ordersPage.getTotal() > 0) {
            for (Orders orders : ordersPage) {
                //Get order id
                Long orderId = orders.getId();

                //Query order details by using order id
                List<OrderDetail> orderDetailList = orderDetailMapper.getDetailsByOrderId(orderId);

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetailList);

                orderVOList.add(orderVO);
            }
        }

        long total = ordersPage.getTotal();
        return new PageResult(total, orderVOList);
    }

    /**
     * Get order details
     *
     * @param id
     * @return
     */
    public OrderVO getOrderDetails(Long id) {
        // Query order by id
        Orders orders = orderMapper.getOrderById(id);

        List<OrderDetail> orderDetailList = orderDetailMapper.getDetailsByOrderId(id);

        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);

        return orderVO;
    }

    /**
     * Cancel order
     *
     * @param id
     */
    public void cancelOrderById(Long id) {
        Orders ordersDB = orderMapper.getOrderById(id);

        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        if(ordersDB.getStatus() > 2){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersDB, orders);
        if(ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)){
            orders.setPayStatus(Orders.REFUND);
        }
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("User cancelled order.");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.updateOrder(orders);
    }

    /**
     * Order again
     *
     * @param id
     */
    public void orderAgain(Long id) {
        long userId = BaseContext.getCurrentId();
        List<OrderDetail> orderDetailList = orderDetailMapper.getDetailsByOrderId(id);

        List<ShoppingCart> cartList = orderDetailList.stream().map(orderDetail -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail, shoppingCart, "id");
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());
            return shoppingCart;
        }).collect(Collectors.toList());

        shoppingCartMapper.insertBatch(cartList);
    }

    //ADMIN

    /**
     * Condition Search
     *
     * @param ordersPageQueryDTO
     * @return
     */
    public PageResult orderConditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);

        List<OrderVO> orderVOList = getOrderList(page);

        return new PageResult(page.getTotal(), orderVOList);
    }

    public OrderStatisticsVO getOrderStatistics() {
        Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
        Integer confirmed = orderMapper.countStatus(Orders.CONFIRMED);
        Integer deliveryInProgress = orderMapper.countStatus(Orders.DELIVERY_IN_PROGRESS);

        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);
        return orderStatisticsVO;
    }

    /**
     * Confirm Order
     * @param ordersConfirmDTO
     */
    public void confirmOrder(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .build();
        orderMapper.updateOrder(orders);
    }

    /**
     * Reject order
     *
     * @param ordersRejectionDTO
     */
    public void rejectOrder(OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        Orders ordersDB = orderMapper.getOrderById(ordersRejectionDTO.getId());

        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Integer payStatus = ordersDB.getPayStatus();
        if(payStatus == Orders.PAID){
            //todo: Refund-related functions
            log.info("Refund.");
        }

        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.CANCELLED)
                .rejectionReason(ordersRejectionDTO.getRejectionReason())
                .payStatus(Orders.REFUND)
                .cancelTime(LocalDateTime.now())
                .build();

        orderMapper.updateOrder(orders);
    }

    /**
     * Cancel order by admin
     *
     * @param ordersCancelDTO
     */
    public void cancelOrder(OrdersCancelDTO ordersCancelDTO) throws Exception {
        Orders ordersDB = orderMapper.getOrderById(ordersCancelDTO.getId());

        Integer payStatus = ordersDB.getPayStatus();
        if(payStatus == Orders.PAID){
            //todo: Refund-related functions
            log.info("Refund.");
        }

        Orders orders = Orders.builder()
                .id(ordersCancelDTO.getId())
                .status(Orders.CANCELLED)
                .rejectionReason(ordersCancelDTO.getCancelReason())
                .payStatus(Orders.REFUND)
                .cancelTime(LocalDateTime.now())
                .build();

        orderMapper.updateOrder(orders);
    }

    /**
     * Delivery order
     *
     * @param id
     */
    public void deliveryOrder(Long id) {
        Orders ordersDB = orderMapper.getOrderById(id);

        if(ordersDB == null || !ordersDB.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.DELIVERY_IN_PROGRESS)
                .build();
        orderMapper.updateOrder(orders);
    }

    /**
     * Complete Order
     *
     * @param id
     */
    public void completeOrder(Long id) {
        Orders ordersDB = orderMapper.getOrderById(id);

        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.COMPLETED)
                .deliveryTime(LocalDateTime.now())
                .build();
        orderMapper.updateOrder(orders);
    }

    /**
     * Send reminder
     *
     * @param id
     */
    public void sendReminder(Long id) {
        Orders orders = orderMapper.getOrderById(id);
        if(orders == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        Map map = new HashMap();
        map.put("type", 2);//2代表用户催单
        map.put("orderId", id);
        map.put("content", "OrderNo：" + orders.getNumber());
        webSocketServer.sendToAllClient(JSON.toJSONString(map));
    }

    private List<OrderVO> getOrderList(Page<Orders> page) {
        List<OrderVO> orderVOList = new ArrayList();

        List<Orders> ordersList = page.getResult();
        if (!CollectionUtils.isEmpty(ordersList)) {
            for (Orders orders : ordersList) {
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                String orderDishes = getOrderDishesStr(orders);
                orderVO.setOrderDishes(orderDishes);
                orderVOList.add(orderVO);
            }
        }
        return orderVOList;
    }

    private String getOrderDishesStr(Orders orders) {
        List<OrderDetail> detailList = orderDetailMapper.getDetailsByOrderId(orders.getId());

        List<String> orderDishList = detailList.stream().map(x -> {
            String orderDish = x.getName() + "*" + x.getNumber() + ";";
            return orderDish;
        }).collect(Collectors.toList());

        return String.join("", orderDishList);
    }


}
