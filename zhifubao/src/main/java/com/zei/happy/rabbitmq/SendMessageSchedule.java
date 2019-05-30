package com.zei.happy.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.zei.happy.bean.Message;
import com.zei.happy.mapper.MessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class SendMessageSchedule {

    private static final String EXCHANGE = "zhifubao.transfer.exchange";

    private static final String ROUTINGKEY = "zhifubao.transfer.routingKey";

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 每10秒发送一次
     */
    @Scheduled(cron = "0/10 0/1 * * * ?")
    public void sendMessage(){
        List<Message> unSendMessages = messageMapper.findUnSend();
        if(unSendMessages != null && unSendMessages.size()>0){
            for (Message message : unSendMessages) {
                log.info("================发送未转账成功的消息"+ message.getMessageId() +"==============" + new Date().toString());
                String jsonStr = JSON.toJSONString(message);
                rabbitTemplate.convertAndSend(EXCHANGE,ROUTINGKEY,jsonStr);
            }
        }
    }
}
