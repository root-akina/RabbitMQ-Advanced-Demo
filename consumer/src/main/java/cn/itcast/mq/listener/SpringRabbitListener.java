package cn.itcast.mq.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class SpringRabbitListener {

    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueue(String msg) {
        System.out.println("消费者接收到simple.queue的消息：【" + msg + "】");
        int i = 1/0;
    }

    /*@RabbitListener(queues = "topic.queue1")
    public void listenSimpleQueue(String msg, Channel channel, Message message) throws IOException {
        try {
            log.debug("消费者接收到simple.queue的消息：【" + msg + "】");
            int i = 1/0;
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
            log.debug("消息处理完成！");
        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),true,true);
            e.printStackTrace();
        }
    }*/

    @RabbitListener(queues = "error.queue")
    public void errorMessage(String msg){
        System.out.println("出现错误信息，请及时处理：{"+msg+"}");
    }


    @RabbitListener(bindings =@QueueBinding(
            value =@Queue(value = "dl.queue"),
            exchange = @Exchange(value = "dl.direct"),
            key = {"dl"}
    ))
    public void deadLMessage(String msg){
        log.debug("deadL message:{}",msg);
    }
}
