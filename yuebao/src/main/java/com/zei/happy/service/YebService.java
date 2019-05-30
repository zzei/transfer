package com.zei.happy.service;

import com.alibaba.fastjson.JSONObject;
import com.oracle.tools.packager.Log;
import com.zei.happy.bean.BackAccount;
import com.zei.happy.bean.BackMessage;
import com.zei.happy.mapper.AccountMapper;
import com.zei.happy.mapper.MessageMapper;
import com.zei.happy.rabbit.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@Service
public class YebService {

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    SendMessage sendMessage;

    @Autowired
    TransactionTemplate transactionTemplate;

    public int saveMoney(int userId,double money,String messageId){
        log.info("=====userId:"+userId + "-----money:"+money + "------messageId:"+messageId);
        //幂等
        //判断消息是否已经存在
        BackMessage message = messageMapper.get(messageId);
        if(message != null){
            //消息已经处理过了
            log.info("------------=该消息已经处理过，不再处理==="+messageId);
            return 0;
        }

        boolean result = (boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                log.info("------------开始处理转账==========");
                //存钱
                BackAccount account = new BackAccount().setUserId(userId).setMoney(money);
                int aResult = accountMapper.saveMoney(account);
                if(1 == aResult){
                    //保存消息
                    int mResult = messageMapper.save(messageId);
                    return 1 == mResult;
                }
                return false;
            }
        });

        //发送消息给支付宝声明转账成功
        if(result){
            log.info("===========余额宝已入账，发送消息给支付宝===========");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("respCode","ok");
            jsonObject.put("messageId",messageId);
            sendMessage.send("yuebao.response.exchange","yuebao.response.routingKey",jsonObject.toJSONString());
        }

        return 1;
    }
}
