package com.hzb.web.websocket.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hzb.web.websocket.common.GlobalConstant;
import com.hzb.web.websocket.common.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.jms.Topic;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author :huangZB
 * @date 2021/4/17
 * @Description  websocker 信息处理器
 */
public class TestHandler implements WebSocketHandler {


	private JmsTemplate jmsTemplateTopic = (JmsTemplate) SpringUtils.getBean("jmsTemplateTopic");
	private Topic userTopic = (Topic) SpringUtils.getBean("userTopic");


	// 定义这个参数用于存放当前正在连接的客户端，如果后期采用异步订阅的方式发送信息可以使用这个
	public static final ConcurrentHashMap<String, CopyOnWriteArraySet<WebSocketSession>> map = new ConcurrentHashMap();

	private static final Logger log = LoggerFactory.getLogger(TestHandler.class);



	/**
	 * 在WebSocket协商成功后调用，并且打开WebSocket连接准备使用，
	 * @param webSocketSession
	 * @throws Exception
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
		// 这里在建立连接后，将建立连接信息存入到map中。
		System.out.println("建立链接");
		String userId = webSocketSession.getAttributes().get(GlobalConstant.SESSION_USER).toString();
		CopyOnWriteArraySet<WebSocketSession> webSocketSessionOne = map.get(userId);
		if(webSocketSessionOne != null){
			// map.put(userId,)
			webSocketSessionOne.add(webSocketSession);
		} else {
			webSocketSessionOne = new CopyOnWriteArraySet<WebSocketSession>();
			webSocketSessionOne.add(webSocketSession);
		}
		map.put(userId,webSocketSessionOne);
		System.out.println("在线人数"+map.size());
	}

	/**
	 * 处理收到的webSocketMessage
	 * @param webSocketSession
	 * @param webSocketMessage
	 * @throws Exception
	 */
	@Override
	public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
		// 通过  webSocketMessage.getPayload()获取发送的信息
		JSONObject jsonObject = JSONObject.parseObject((String) webSocketMessage.getPayload());
		// 实现消息的发送
		log.info("接收到信息{}",jsonObject);
		jmsTemplateTopic.convertAndSend(userTopic, JSON.toJSONString(jsonObject));
		log.info("信息发送完毕！");
	}

	/**
	 * 处理来自底层WebSocket消息传输的错误
	 * @param webSocketSession
	 * @param throwable
	 * @throws Exception
	 */
	@Override
	public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

		// 关闭链接
		if(webSocketSession != null && webSocketSession.isOpen()){
			webSocketSession.close();
		}
		// 移除session
		removeSession(webSocketSession);
		System.out.println("---------消息传输的错误---------");
	}

	/**
	 * 在网络套接字连接关闭后或在传输错误发生后调用。
	 * 尽管从技术上讲，会话可能仍然是开放的，但取决于底层实现，在这一点上发送消息是不鼓励的，而且很可能不会成功。
	 * @param webSocketSession
	 * @param closeStatus
	 * @throws Exception
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
		// 关闭链接
		if(webSocketSession != null && webSocketSession.isOpen()){
			webSocketSession.close();
		}
		// 移除session
		removeSession(webSocketSession);
		System.out.println("---------安全退出了websocket---------");
	}

	/**
	 *  WebSocketHandler是否处理部分消息
	 * @return
	 */
	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	private void removeSession(WebSocketSession webSocketSession){
		// 获取这个用户的 所有在线用户组
		String userId = webSocketSession.getAttributes().get(GlobalConstant.SESSION_USER).toString();
		CopyOnWriteArraySet<WebSocketSession> webSocketSessionOne = map.get(userId);
		webSocketSessionOne.remove(webSocketSession);
	}
}
