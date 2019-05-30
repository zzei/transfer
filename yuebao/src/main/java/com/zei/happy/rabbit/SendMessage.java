package com.zei.happy.rabbit;

import com.alibaba.fastjson.JSONObject;
import com.zei.happy.service.YebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class SendMessage {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    YebService yebService;

    /**
     * 发送信息到队列中
     * @param exchange
     * @param routingKey
     * @param jsonStr
     */
    public void send(String exchange,String routingKey,String jsonStr){
        log.info("================发送转账成功消息给支付宝==============" + new Date().toString());
        rabbitTemplate.convertAndSend(exchange,routingKey,jsonStr);
    }

    /**
     * 接收转账消息
     * @param jsonStr
     */
    @RabbitListener(queues = "zhifubao.transfer.queue")
    public void receive(String jsonStr){
      log.info("-===============余额宝接收到转账消息==============");
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        int userId = Integer.parseInt(jsonObject.getString("userId"));
        double money = Double.parseDouble(jsonObject.getString("money"));
        String messageId = jsonObject.getString("messageId");
        yebService.saveMoney(userId,money,messageId);

    }
}
