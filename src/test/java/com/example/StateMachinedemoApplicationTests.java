package com.example;

import com.example.StateMachinedemoApplication;
import com.example.bean.Order;
import com.example.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = StateMachinedemoApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class StateMachinedemoApplicationTests {
    @Autowired
    private OrderService orderService;

    @Test
    void contextLoads() throws Exception {
        Order order = orderService.pay(1);
        log.info("" + order.getStatus());
    }


}
