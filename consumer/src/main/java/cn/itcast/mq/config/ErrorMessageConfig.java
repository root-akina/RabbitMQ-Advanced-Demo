package cn.itcast.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorMessageConfig {

    @Bean
    public DirectExchange directError(){
        return new DirectExchange("error.direct");
    }

    @Bean
    public Queue errorQueue(){
        return new Queue("error.queue");
    }

    @Bean
    public Binding errorMessageQueue(Queue errorQueue,DirectExchange directError){
        return BindingBuilder.bind(errorQueue).to(directError).with("error");
    }

    //实现错误信息的转出，转入
    @Bean
    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate){
        return new RepublishMessageRecoverer(rabbitTemplate,"error.direct","error");
    }
}
