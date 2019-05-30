package com.zei.happy.mapper;

import com.zei.happy.bean.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface MessageMapper {

    @Insert("insert into message(message_id,user_id,money,send_state) values(#{messageId},#{userId},#{money},#{sendState})")
    int save(Message message);

    @Update("update message set send_state = #{sendState} where message_id = #{messageId}")
    int updateState(Message message);

    @Select("select * from message where send_state = 0")
    List<Message> findUnSend();
}
