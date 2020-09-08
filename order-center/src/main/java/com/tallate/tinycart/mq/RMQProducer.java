package com.tallate.tinycart.mq;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RMQProducer {

    @Resource
    private TransactionMQProducer producer;

    public void sendMessage(String data, String topic, String tag, String key) {
        try {
            byte[] messageBody = data.getBytes(RemotingHelper.DEFAULT_CHARSET);
            if (StringUtils.isBlank(key)) {
                key = System.currentTimeMillis() + "";
            }
            Message message = new Message(topic, tag, key, messageBody);
            TransactionSendResult sendResult = producer.sendMessageInTransaction(message, null);
            if (SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
                log.info("发送消息成功, topic:{}, tag:{}, state:{}", topic, tag, sendResult.getSendStatus());
            } else {
                log.warn("发送消息失败, topic:{}, tag:{}, state:{}", topic, tag, sendResult.getSendStatus());
            }
        } catch (Exception e) {
            log.warn("发消息出错, topic:{}, tag:{}", topic, tag, e);
            // 增加报警等机制
        }
    }
}
