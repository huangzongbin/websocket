package com.hzb.web.websocket.config;

import com.hzb.web.websocket.handler.TestHandler;
import com.hzb.web.websocket.interceptor.TestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @Author :huangZB
 * @date 2021/4/17
 * @Description 这里集成一下websocket 的配置，注入拦截器
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new TestHandler(),"/ws/testSocket")
		        // 设置允许跨域
		.setAllowedOrigins("*")
				// 添加拦截器
		.addInterceptors(new TestInterceptor());
	}
}
