package com.to8to.tbt.msc.configuration;

import com.to8to.tbt.msc.constant.MsgSendConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author juntao.guo
 */
@Slf4j
@EnableRabbit
@Configuration
public class RabbitMqConfiguration {

    @Bean
    public Queue asyncMessage(){
        return new Queue(MsgSendConstant.MQ_ASYNC_MESSAGE_QUEUE, true);
    }

    @Bean
    public Queue asyncAppNote(){
        return new Queue(MsgSendConstant.MQ_ASYNC_APP_NOTE_QUEUE, true);
    }
}
