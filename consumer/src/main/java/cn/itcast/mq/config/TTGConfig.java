package cn.itcast.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TTGConfig {

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("ttl.direct");
    }

    @Bean
    public Queue queue(){
        return QueueBuilder.durable("ttl.queue")
                .ttl(20000)
                .deadLetterExchange("dl.direct")
                .deadLetterRoutingKey("dl").build();
    }

    @Bean
    public Binding binding(Queue queue,DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with("ttl");

    }

}
