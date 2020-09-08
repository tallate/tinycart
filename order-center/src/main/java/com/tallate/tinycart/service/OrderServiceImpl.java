package com.tallate.tinycart.service;

import com.alibaba.fastjson.JSON;
import com.tallate.tinycart.bean.Order;
import com.tallate.tinycart.bean.OrderSaveReq;
import com.tallate.tinycart.dao.OrderDao;
import com.tallate.tinycart.mq.RMQProducer;
import java.util.UUID;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private RMQProducer rmqProducer;

    @Value("${rmq.topic.orderCreated}")
    private String orderCreatedTopic;

    @Resource
    private OrderDao orderDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doSave(Order order) {
        orderDao.save(order);
    }

    @Override
    public String save(OrderSaveReq req) {
        Order order = Order.builder()
                .orderNo(UUID.randomUUID().toString())
                .userName(req.getUserName())
                .goodsList(req.getGoodsList())
                .build();
        // 发事务消息
        rmqProducer.sendMessage(
                JSON.toJSONString(order), orderCreatedTopic, "tag", "key");
        return order.getOrderNo();
    }

    @Override
    public Order query(String orderNo) {
        return orderDao.query(orderNo);
    }
}
