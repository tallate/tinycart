package com.tallate.tinycart.mq;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RMQConfig {

    @Value("${rmq.namesrv.addr}")
    private String namesrvAddr;

    @Value("${rmq.group.order}")
    private String orderGroup;

    @Value("${rmq.instanceName.order}")
    private String orderInstanceName;

    @Resource
    private OrderTxListener orderTxListener;

    private static final ExecutorService TX_EXECUTOR_THREAD_POOL =
            new ThreadPoolExecutor(2 * Runtime.getRuntime().availableProcessors(),
                    2 * Runtime.getRuntime().availableProcessors() + 1,
                    10, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(10),
                    r -> new Thread(r, "rmq-tx"));

    @Bean(initMethod = "start", destroyMethod = "shutdown", name = "sendDefaultProducer")
    public TransactionMQProducer producer() {
        TransactionMQProducer producer = new TransactionMQProducer(orderGroup);
        producer.setNamesrvAddr(namesrvAddr);
        producer.setInstanceName(orderInstanceName);
        // 发送失败自动重试次数
        producer.setRetryTimesWhenSendFailed(3);
        producer.setExecutorService(TX_EXECUTOR_THREAD_POOL);
        producer.setTransactionListener(orderTxListener);
        log.info("producer启动, namesrvAddr:{}, orderGroup:{}, orderInstanceName:{}", namesrvAddr, orderGroup, orderInstanceName);
        return producer;
    }
}
