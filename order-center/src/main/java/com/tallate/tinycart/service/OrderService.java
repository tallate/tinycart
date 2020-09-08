package com.tallate.tinycart.service;

import com.tallate.tinycart.bean.Order;
import com.tallate.tinycart.bean.OrderSaveReq;

public interface OrderService {

    void doSave(Order order);

    String save(OrderSaveReq req);

    Order query(String orderNo);
}
