package com.example.service;

import com.example.bean.Order;
import com.example.enums.OrderState;
import com.example.enums.OrderStateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

@Service("orderService")
public class OrderService {
    @Autowired
    private StateMachine<OrderState, OrderStateEvent> orderStateMachine;
    @Autowired
    private StateMachinePersister<OrderState, OrderStateEvent, Order> orderStateMachinePersister;

    public Order pay(int orderId) throws Exception {
        Order order = new Order();
        order.setStatus(OrderState.WAIT_PAY);
        order.setId(orderId);
        if (!sendEvent(OrderStateEvent.PAY, order)) {
            throw new Exception("状态异常" + order);
        }
        return order;
    }
    public Order recive(int orderId) throws Exception {
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderState.PAID);
        if (!sendEvent(OrderStateEvent.RECIVE, order)) {
            throw new Exception("状态异常" + order);
        }
        return order;
    }

    /**
     * 发送订单状态转换事件
     *
     * @param event 事件
     * @param order 订单
     * @return 执行结果
     */
    private boolean sendEvent(OrderStateEvent event, Order order) {
        boolean result = false;
        try {
            orderStateMachine.start();
            // 设置状态机状态
            orderStateMachinePersister.restore(orderStateMachine, order);
            result = orderStateMachine.sendEvent(MessageBuilder.withPayload(event).setHeader("order", order).build());
            // 保存状态机状态
            orderStateMachinePersister.persist(orderStateMachine, order);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            orderStateMachine.stop();
        }
        return result;
    }
}
