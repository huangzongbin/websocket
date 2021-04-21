package com.hzb.web.activemq.consumer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * @Author :huangZB
 * @date 2021/4/19
 * @Description
 */
@Component
public class ActiveMqConsumer {

	private final static Logger log = LoggerFactory.getLogger(ActiveMqConsumer.class);
	/**
	 * 消费主题消息
	 *
	 *  由于我们设置了监听模式，当有信息向这个主题发送消息的时候，监听机制启动，主动接受信息。
	 * @param message
	 */
	@JmsListener(destination = "defaultTopic", containerFactory = "jmsListenerContainerTopic")
	public void receiveTopic(TextMessage message, Session session) throws JMSException {
		try {
			log.debug("接收到defaultTopic消息===========》{}", message.getText());
			throw new RuntimeException("操作异常");
		} catch (Exception e) {
			session.recover();
		}
	}

	/**
	 * 消费队列消息
	 *
	 * @param message
	 */
	@JmsListener(destination = "defaultQueue", containerFactory = "jmsListenerContainerQueue")
	public void receiveQueue(TextMessage message, Session session) throws JMSException {
		try {
			log.debug("接收到defaultQueue消息===========》{}", message.getText());
			message.acknowledge();
			throw new RuntimeException("操作异常");
		} catch (Exception e) {
			session.recover();
		}
	}

}
