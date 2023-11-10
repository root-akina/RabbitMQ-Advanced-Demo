package cn.itcast.mq.spring;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void advancedMQ(){
        //1.交换机,key,消息
        String exchangeName = "hmall.topic";
        String routingKey = "";
        String msg = "advanced MQ.. hello";
        //2.创建CorrelationData
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());
        correlationData.getFuture().addCallback(new SuccessCallback<CorrelationData.Confirm>() {
            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                if (result.isAck()) {
                    log.info("消息成功投递到交换机：ack 交换机ID：{}", correlationData.getId());
                } else {
                    log.info("消息未投递到交换机：nack，交换机ID：{}，失败原因：{}", correlationData.getId(), result.getReason());
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("消息异常//投递到交换机，但没有路由到队列，ID：{}，原因：{}",correlationData.getId(),ex.getMessage());
            }
        });
        //3.发送消息
        rabbitTemplate.convertAndSend(exchangeName,routingKey,msg,correlationData);
    }

    @Test
    public void simpleQueue(){
        String queueName = "simple.queue";
        String msg = "hello";
        rabbitTemplate.convertAndSend(queueName,msg);
    }

    @Test
    public void ttlMessage(){
        String exchangeName = "ttl.direct";
        String routingKey = "ttl";
        Message msg = MessageBuilder.withBody("hello from ttg".getBytes(StandardCharsets.UTF_8))
                .setExpiration("30000").build();  //20s

        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());

        rabbitTemplate.convertAndSend(exchangeName,routingKey,msg,correlationData);
        log.info("send");
    }
}
