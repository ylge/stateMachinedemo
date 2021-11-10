package com.example.controller;

import com.example.bean.Order;
import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order/")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping("pay/{orderId}")
    public Order pay(@PathVariable int orderId) throws Exception {
        return orderService.pay(orderId);
    }

    @RequestMapping("recive/{orderId}")
    public Order recive(@PathVariable int orderId) throws Exception {
        return orderService.recive(orderId);
    }
}
