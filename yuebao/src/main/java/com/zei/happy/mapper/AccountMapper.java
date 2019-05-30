package com.zei.happy.mapper;

import com.zei.happy.bean.BackAccount;
import org.apache.ibatis.annotations.Update;

public interface AccountMapper {

    @Update("update back_account set money = money + #{money} where user_id = #{userId}")
    int saveMoney(BackAccount account);
}
