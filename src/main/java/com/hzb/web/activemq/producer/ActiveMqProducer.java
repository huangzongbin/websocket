package com.hzb.web.activemq.producer;

import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;

/**
 * @Author :huangZB
 * @date 2021/4/19
 * @Description
 */
@Component
public class ActiveMqProducer {

	@Autowired
	@Qualifier("jmsTemplateTopic")
	private JmsTemplate jmsTemplateTopic;

	@Autowired
	@Qualifier("jmsTemplateQueue")
	private JmsTemplate jmsTemplateQueue;

	@Autowired
	@Qualifier("defaultTopic")
	private Topic defaultTopic;

	@Autowired
	@Qualifier("defaultQueue")
	private Queue defaultQueue;

	public void send(){
		jmsTemplateQueue.convertAndSend(defaultTopic,"发送topic消息");
		jmsTemplateTopic.convertAndSend(defaultQueue,"发送query消息");
	}

	/**
	 * 发送消息
	 *
	 * @param destination 目标
	 * @param message     内容，一般为JSON
	 */
	public void send(Destination destination, String message) {
		jmsTemplateQueue.convertAndSend(destination, message);
	}

}
