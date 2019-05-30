package com.zei.happy.mapper;

import com.zei.happy.bean.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

public interface AccountMapper {

    @Insert("insert into account(user_id,money) values(#{userId},#{money})")
    @Options(useGeneratedKeys = true, keyColumn = "id",keyProperty = "id")
    int save(Account account);

    @Update("update account set money = money - #{money} where user_id = #{userId}")
    int transfer(Account account);
}
