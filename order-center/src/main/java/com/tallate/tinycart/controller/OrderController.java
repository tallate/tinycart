package com.tallate.tinycart.controller;

import com.tallate.tinycart.bean.Order;
import com.tallate.tinycart.bean.OrderSaveReq;
import com.tallate.tinycart.service.OrderServiceImpl;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderServiceImpl orderServiceImpl;

    @PostMapping("/save")
    public String saveOrder(@RequestBody OrderSaveReq req) {
        return orderServiceImpl.save(req);
    }

    @GetMapping("/query")
    public Order queryOrder(@RequestParam String orderNo) {
        return orderServiceImpl.query(orderNo);
    }

}
