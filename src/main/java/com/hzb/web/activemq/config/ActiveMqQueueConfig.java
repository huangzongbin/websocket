package com.hzb.web.activemq.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;


/**
 * @Author 黄宗滨
 * @Description queue模式初始配置
 * @Date  2021/4/19
 **/
@Configuration
public class ActiveMqQueueConfig {


    /**
     * 定义死信队列
     */
    @Bean(name = "dlqQueue")
    public Queue dlqQueue() {
        return new ActiveMQQueue("ActiveMQ.DLQ");
    }

    /**
     * 定义队列
     */
    @Bean(name = "defaultQueue")
    public Queue defaultQueue() {
        return new ActiveMQQueue("defaultQueue");
    }


}
