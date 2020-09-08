package com.tallate.tinycart.mq;

import com.alibaba.fastjson.JSON;
import com.tallate.tinycart.bean.Order;
import com.tallate.tinycart.service.OrderService;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderTxListener implements TransactionListener {

    @Resource
    private OrderService orderService;

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try {
            log.info("执行本地事务开始");
            Order order = JSON.parseObject(msg.getBody(), Order.class);
            orderService.doSave(order);
            log.info("执行本地事务完毕, order:{}", order);
            return LocalTransactionState.COMMIT_MESSAGE;
        } catch (Exception e) {
            log.warn("执行本地事务出错", e);
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        log.info("检查本地事务状态开始");
        Order order = JSON.parseObject(msg.getBody(), Order.class);
        if (null == order || StringUtils.isBlank(order.getOrderNo())) {
            log.warn("消息格式不正确, order:{}", order);
            return LocalTransactionState.UNKNOW;
        }
        String orderNo = order.getOrderNo();
        Order dbOrder = orderService.query(orderNo);
        if (dbOrder != null) {
            log.info("订单落库正常, dbOrder:{}", dbOrder);
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        log.warn("订单未正常落库, orderNo:{}", orderNo);
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }
}
