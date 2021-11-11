package com.example.enums;

public enum OrderState {
    //待支付，已支付，已取货
    WAIT_PAY(0),PAID(1),RECIVED(3);
    private Integer status;

    OrderState(int status) {
        this.status = status;
    }
}
