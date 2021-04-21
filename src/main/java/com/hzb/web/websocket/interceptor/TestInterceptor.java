package com.hzb.web.websocket.interceptor;

import com.hzb.web.websocket.common.GlobalConstant;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * @Author :huangZB
 * @date 2021/4/17
 * @Description  websocket 拦截器
 */

public class TestInterceptor implements HandshakeInterceptor {
	public TestInterceptor() {
		super();
	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {

		// 我们可以看到在处理信息前有一个map的参数，后面的Handler可以直接从这里的map拿参数。
		// handle 里面通过 webSocketSession.getAttributes().get("map的key").toString();
		if(serverHttpRequest instanceof ServletServerHttpRequest){
			String  userId = ((ServletServerHttpRequest)serverHttpRequest).getServletRequest().getParameter("userId");
			if (userId !="") {
				// 这里将数据存入到数组中
				map.put(GlobalConstant.SESSION_USER,userId);
				System.out.println("开始了");
			}else{
				return false;
			}
		}
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
		System.out.println("--------进入webSocket的afterHandshake拦截器！----------");
	}
}
