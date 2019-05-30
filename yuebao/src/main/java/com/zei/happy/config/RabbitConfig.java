package com.zei.happy.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean(name = "respQueue")
    public Queue respQueue(){
        return new Queue("yuebao.response.queue");
    }

    @Bean
    public DirectExchange respExchange(){
        return new DirectExchange("yuebao.response.exchange");
    }

    @Bean
    public BindingBuilder.GenericArgumentsConfigurer respBinding(@Qualifier("respQueue") Queue queue, Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("yuebao.response.routingKey");
    }
}
