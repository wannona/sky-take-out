package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务类，定时处理任务订单
 */
@Component
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder(){
        List<Orders> ordersLs = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT,
                LocalDateTime.now().plusMinutes(-15));

        if(ordersLs!=null && ordersLs.size()>0){
            for(Orders orders : ordersLs){
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时, 自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        List<Orders> ordersLs = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS,
                LocalDateTime.now().plusHours(-1));

        if(ordersLs!=null && ordersLs.size()>0){
            for(Orders orders : ordersLs){
                orders.setStatus(Orders.COMPLETED);
                orders.setCancelReason("订单超时, 自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }
}
