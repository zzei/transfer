package com.zei.happy.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zei.happy.bean.Account;
import com.zei.happy.bean.Message;
import com.zei.happy.mapper.AccountMapper;
import com.zei.happy.mapper.MessageMapper;
import com.zei.happy.rabbitmq.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;

@Service
public class TransferServiceImpl implements TransferService{

    private static final String EXCHANGE = "zhifubao.transfer.exchange";

    private static final String ROUTINGKEY = "zhifubao.transfer.routingKey";


    @Autowired
    AccountMapper accountMapper;

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    TransactionTemplate transactionTemplate;

    @Autowired
    SendMessageService sendMessageService;

    @Override
    public int save(Account account) {

        return accountMapper.save(account);
    }

    /**
     * 支付宝转余额宝
     * @param userId
     * @param money
     * @return
     */
    @Override
    public int transfer(int userId, double money) {
        //先扣除本地支付宝金额
        String messageStr = (String) transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                Account account = new Account().setUserId(userId).setMoney(money);
                int result = accountMapper.transfer(account);
                if(1 == result){
                    //本地扣除成功 创建发送消息队列的消息
                    Message message = new Message();
                    String messageId = userId + "zzei" + UUID.randomUUID().toString().substring(0,12);
                    message.setMessageId(messageId);
                    message.setMoney(money);
                    message.setUserId(userId);
                    message.setSendState(0);
                    int messageResult = messageMapper.save(message);
                    if(1 == messageResult){
                        return messageId;
                    }
                }

                return null;
            }
        });

        if (messageStr == null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId",userId);
            jsonObject.put("money",money);
            jsonObject.put("messageId",messageStr);
            //去发送消息队列
            sendMessageService.send(EXCHANGE,ROUTINGKEY,jsonObject.toJSONString());
        }


        return 1;
    }


    @Transactional
    @Override
    public int updateMessageState(String messageId, int sendState) {
        Message message = new Message().setMessageId(messageId).setSendState(sendState);
        return messageMapper.updateState(message);
    }
}
