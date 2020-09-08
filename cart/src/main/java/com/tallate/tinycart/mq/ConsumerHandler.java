package com.tallate.tinycart.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.tallate.tinycart.bean.Order;
import com.tallate.tinycart.service.CartService;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerHandler implements MessageListenerConcurrently {

    @Resource
    private CartService cartService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        try {
            log.info("开始处理消息");
            for (MessageExt msg : msgs) {
                Order order = JSON.parseObject(msg.getBody(), Order.class);
                if (order == null) {
                    continue;
                }
                cartService.remove(order);
            }
            log.info("消息处理完毕");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (JSONException e) {
            log.error("JSON格式不对, msgs:{}", msgs, e);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            log.error("消息处理出错, msgs:{}", msgs, e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}
