package com.hzb.web.activemq.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;


import javax.jms.ConnectionFactory;

/**
 * @Author :huangZB
 * @date 2021/4/19
 * @Description  这里对于activemq 第配置进行设置，设置了queue 和topic的一些属性和设置监听机制
 */
@EnableJms
@Configuration
public class activeMqConfig {

	@Bean(name = "connectionFactory")
	@Primary
	public ConnectionFactory connectionFactory(
			@Value("${spring.activemq.broker-url}") String brokerURL,
			@Value("${spring.activemq.user}") String userName,
			@Value("${spring.activemq.password}") String password,
			RedeliveryPolicy redeliveryPolicy){
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(userName,password,brokerURL);
		factory.setTrustAllPackages(true);
		// 这里采用消息的异常发送方式
		factory.setUseAsyncSend(true);
		// 设置消息的重发机制
		factory.setRedeliveryPolicy(redeliveryPolicy);
		CachingConnectionFactory cachingFactory = new CachingConnectionFactory(factory);
		cachingFactory.setSessionCacheSize(50);

		return cachingFactory;

	}


	/**
	 * topic模式的jmsTemplate
	 *
	 * @param connectionFactory
	 */
	@Bean(name = "jmsTemplateTopic")
	public JmsTemplate jmsTemplateTopic(ConnectionFactory connectionFactory) {
		JmsTemplate jmsTemplate = new JmsTemplate();
		//进行持久化配置 1表示非持久化，2表示持久化
		jmsTemplate.setDeliveryMode(2);
		jmsTemplate.setPubSubDomain(true);
		//客户端签收模式
		jmsTemplate.setSessionAcknowledgeMode(4);
		jmsTemplate.setConnectionFactory(connectionFactory);
		return jmsTemplate;
	}


	/**
	 * queue模式的JmsTemplate
	 *
	 * @param connectionFactory
	 */
	@Bean(name = "jmsTemplateQueue")
	public JmsTemplate jmsTemplateQueue(ConnectionFactory connectionFactory) {
		JmsTemplate jmsTemplate = new JmsTemplate();
		//进行持久化配置 1表示非持久化，2表示持久化
		jmsTemplate.setDeliveryMode(2);
		//客户端签收模式
		jmsTemplate.setSessionAcknowledgeMode(4);
		jmsTemplate.setConnectionFactory(connectionFactory);
		return jmsTemplate;
	}


	/**
	 * topic模式的ListenerContainer
	 * 监听机制
	 *
	 * @param connectionFactory
	 */
	@Bean(name = "jmsListenerContainerTopic")
	public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setPubSubDomain(true);
		//重连间隔时间
		factory.setSessionAcknowledgeMode(4);
		factory.setConnectionFactory(connectionFactory);
		return factory;
	}

	/**
	 * queue模式的ListenerContainer
	 * 监听机制
	 * @param connectionFactory
	 */
	@Bean(name = "jmsListenerContainerQueue")
	public JmsListenerContainerFactory<?> jmsListenerContainerQueue(ConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		//重连间隔时间
		factory.setSessionAcknowledgeMode(4);
		factory.setConnectionFactory(connectionFactory);
		return factory;
	}

	/**
	 * 消息重发
	 */
	@Bean
	public RedeliveryPolicy redeliveryPolicy() {
		RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
		//是否在每次尝试重新发送失败后,增长这个等待时间
		redeliveryPolicy.setUseExponentialBackOff(true);
		//重发次数,默认为6次   这里设置为5次
		redeliveryPolicy.setMaximumRedeliveries(10);
		//重发时间间隔,默认为1秒
		redeliveryPolicy.setInitialRedeliveryDelay(1000);
		//第一次失败后重新发送之前等待1秒,第二次失败再等待1 * 2秒,这里的2就是value
		redeliveryPolicy.setBackOffMultiplier(2);
		//是否避免消息碰撞
		redeliveryPolicy.setUseCollisionAvoidance(false);
		//设置重发最大拖延时间-1 表示没有拖延只有UseExponentialBackOff(true)为true时生效
		redeliveryPolicy.setMaximumRedeliveryDelay(-1);
		return redeliveryPolicy;
	}
}
