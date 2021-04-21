package com.hzb.web.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.hzb.web.websocket.handler.TestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author :huangZB
 * @date 2021/4/19
 * @Description
 */
@Component
public class WebTopicConsumer {

	private final static Logger log = LoggerFactory.getLogger(WebTopicConsumer.class);


	@JmsListener(destination = "userTopic",containerFactory = "jmsListenerContainerTopic")
	public void receiverTopic(TextMessage message, Session session ) throws JMSException, IOException {
		log.info("userTopic================>{}",message.getText());
		JSONObject object = JSON.parseObject(message.getText());
		String userId = object.getString("userId");
		if(!"".equals(userId) && null != userId){
			CopyOnWriteArraySet<WebSocketSession> webSocketSessions = TestHandler.map.get(userId);
			if(webSocketSessions!= null){
				for (WebSocketSession webSocketSession : webSocketSessions) {
					if(webSocketSession.isOpen()){
						webSocketSession.sendMessage(new org.springframework.web.socket.TextMessage(object.getString("msg")));
					}else{
						webSocketSessions.remove(webSocketSession);
					}
				}
			}
		}


	}
}
