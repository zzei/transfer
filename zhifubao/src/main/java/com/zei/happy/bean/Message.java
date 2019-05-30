package com.zei.happy.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Message implements Serializable{

    private String messageId;

    private Integer userId;

    private Double money;

    private Integer sendState;
}
