package com.zei.happy.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {

    @Bean(name = "transferQueue")
    public Queue transferQueue(){
        return new Queue("zhifubao.transfer.queue");
    }

    @Bean
    public DirectExchange transferExchange(){
        return new DirectExchange("zhifubao.transfer.exchange");
    }

    @Bean
    public BindingBuilder.GenericArgumentsConfigurer transferBinding(@Qualifier("transferQueue")Queue transferQueue, Exchange transferExchange){
        return BindingBuilder.bind(transferQueue).to(transferExchange).with("zhifubao.transfer.routingKey");
    }
}
