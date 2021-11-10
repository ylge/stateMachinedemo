package com.example.bean;

import com.example.enums.OrderState;
import lombok.Data;

@Data
public class Order {
    private Integer id;
    private OrderState status;
}
