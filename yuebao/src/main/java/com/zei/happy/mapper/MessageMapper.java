package com.zei.happy.mapper;

import com.zei.happy.bean.BackMessage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface MessageMapper {

    @Insert("insert into back_message(message_id) values(#{messageId})")
    int save(String messageId);

    @Select("select * from back_message where message_id = #{messageId}")
    BackMessage get(String messageId);
}
