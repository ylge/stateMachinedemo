package com.example.listener;

import com.example.enums.OrderStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

/**
 * 订单状态监听器
 */
@Component
@WithStateMachine(name = "orderStateMachine")
@Slf4j
public class OrderStateListener {

    @OnTransition(source = "WAIT_PAY", target = "PAID")
    public boolean deliverTransition(Message<OrderStateEvent> message) {
        log.info(message.getPayload().name());
        log.info(message.getHeaders().get("order").toString());
        System.out.println("待支付 --支付--> 已支付");
        return true;
    }

    @OnTransition(source = "PAID", target = "RECIVED")
    public boolean receiveTransition(Message<OrderStateEvent> message) {
        System.out.println("待收货 --收货--> 完成");
        return true;
    }
}
