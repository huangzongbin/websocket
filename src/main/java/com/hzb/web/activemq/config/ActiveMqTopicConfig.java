package com.hzb.web.activemq.config;

import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Topic;

/**
 * @Author 黄宗滨
 * @Description
 * @Date  2021/4/19
 **/
@Configuration
public class ActiveMqTopicConfig {

    /**
     * 定义主题
     */
    @Bean(name = "defaultTopic")
    public Topic defaultTopic() {
        return new ActiveMQTopic("defaultTopic");
    }

  /*  *//**
     * 平板签字PC端队列
     *//*
    @Bean(name = "signUserTopic")
    public Topic signUserTopic() {
        return new ActiveMQTopic("signUserTopic");
    }

    *//**
     * 平板签字PAD端队列
     *//*
    @Bean(name = "signPadTopic")
    public Topic signPadTopic() {
        return new ActiveMQTopic("signPadTopic");
    }*/

    /**
     * WS队列
     */
    @Bean(name = "userTopic")
    public Topic userTopic() {
        return new ActiveMQTopic("userTopic");
    }

}
