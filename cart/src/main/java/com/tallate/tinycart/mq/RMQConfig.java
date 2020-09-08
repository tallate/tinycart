package com.tallate.tinycart.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RMQConfig {

    @Value("${rmq.namesrv.addr}")
    private String namesrvAddr;

    @Value("${rmq.topic.orderCreated}")
    private String cartTopic;

    @Value("${rmq.group.cart}")
    private String cartGroup;

    @Value("${rmq.instanceName.cart}")
    private String cartInstanceName;

    @Bean(initMethod = "start", destroyMethod = "shutdown", name = "dataConsumer")
    public DefaultMQPushConsumer dataConsumer(ConsumerHandler consumerHandler) throws Exception {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(cartGroup);

        log.info("MQ：starting consultDataConsumer ......");
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setInstanceName(cartInstanceName);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.subscribe(cartTopic, "*");
        consumer.registerMessageListener(consumerHandler);
        consumer.setMaxReconsumeTimes(6);
        consumer.setConsumeThreadMax(6);
        consumer.setConsumeThreadMin(2);
        consumer.setConsumeTimeout(5);
        consumer.setPullThresholdForQueue(50);
        consumer.setPullThresholdForTopic(50);
        log.info("MQ：start consultDataConsumer is success topic:{}, group:{}", cartTopic, cartGroup);

        return consumer;
    }

}
