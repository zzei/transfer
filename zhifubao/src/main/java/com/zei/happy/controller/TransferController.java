package com.zei.happy.controller;

import com.zei.happy.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferController {

    @Autowired
    TransferService transferService;

    @GetMapping("/transfer")
    public String transfer(int userId, double money){
        transferService.transfer(userId,money);

        return "转账成功！";
    }

}
