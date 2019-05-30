package com.zei.happy.service;

import com.zei.happy.bean.Account;

import java.util.List;

public interface TransferService {

    int save(Account account);

    int transfer(int userId, double money);

    int updateMessageState(String messageId,int sendState);

}
