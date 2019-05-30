package com.zei.happy.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zei.happy.service.TransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class SendMessageService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    TransferService transferService;

    /**
     * 发送信息到队列中
     * @param exchange
     * @param routingKey
     * @param jsonStr
     */
    public void send(String exchange,String routingKey,String jsonStr){
        log.info("================开始转账到余额宝==============" + new Date().toString());
        rabbitTemplate.convertAndSend(exchange,routingKey,jsonStr);
    }

    @RabbitListener(queues = "yuebao.response.queue")
    public void receive(String jsonStr){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        String result = jsonObject.getString("respCode");
        if("ok".equals(result)){
            //余额宝已收到消息 并转入金额
            log.info("================收到余额宝转入成功消息=============");
            String messageId = jsonObject.getString("messageId");
            //更新消息状态
            transferService.updateMessageState(messageId,1);
        }
    }
}
