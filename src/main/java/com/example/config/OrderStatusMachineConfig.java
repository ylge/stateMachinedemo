package com.example.config;

import com.example.bean.Order;
import com.example.enums.OrderState;
import com.example.enums.OrderStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableStateMachine(name = "orderStateMachine")
public class OrderStatusMachineConfig extends StateMachineConfigurerAdapter<OrderState, OrderStateEvent> {
    private static Map<Integer, OrderState> orderMap = new HashMap<>();

    /**
     * 配置状态
     *
     * @param states
     * @throws Exception
     */
    @Override
    public void configure(StateMachineStateConfigurer<OrderState, OrderStateEvent> states) throws Exception {
        states.withStates()
                .initial(OrderState.WAIT_PAY)
                .states(EnumSet.allOf(OrderState.class));
    }

    /**
     * 配置事件状态转换
     *
     * @param transitions
     * @throws Exception
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderStateEvent> transitions) throws Exception {
        transitions.withExternal().source(OrderState.WAIT_PAY).target(OrderState.PAID).event(OrderStateEvent.PAY)
                .and()
                .withExternal().source(OrderState.PAID).target(OrderState.RECIVED).event(OrderStateEvent.RECIVE);
    }

    /**
     * 持久化
     * 实际使用可以使用redis
     *
     * @return
     */
    @Bean
    public StateMachinePersister<OrderState, OrderStateEvent, Order> persist() {
        return new DefaultStateMachinePersister<>(new StateMachinePersist<OrderState, OrderStateEvent, Order>() {
            @Override
            public void write(StateMachineContext<OrderState, OrderStateEvent> context, Order order) throws Exception {
                //T进行持久化操作
                orderMap.put(order.getId(), context.getState());
                order.setStatus(context.getState());
            }

            @Override
            public StateMachineContext<OrderState, OrderStateEvent> read(Order order) throws Exception {
                //读取持久化数据返回，此处先返回Order中的数据
                return orderMap.containsKey(order.getId()) ?
                        new DefaultStateMachineContext<>(orderMap.get(order.getId()), null, null, null) :
                        new DefaultStateMachineContext<>(OrderState.WAIT_PAY, null, null, null);
            }
        });
    }
}
