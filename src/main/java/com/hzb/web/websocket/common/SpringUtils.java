package com.hzb.web.websocket.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @Author :huangZB
 * @date 2021/4/20
 * @Description 这里将继承BeanFactoryPostProcessor 进行获取bean的方式，这个方法还可以实现对于bean的修改
 */
@Component
public class SpringUtils implements BeanFactoryPostProcessor {

	private static ConfigurableListableBeanFactory beanFactory;



	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory a) throws BeansException {
		beanFactory = a;
	}

	/**
	 *  获取bean的类型
	 * @param a
	 * @return
	 */
	public static Class<?> getType(String a) throws NoSuchBeanDefinitionException {
		return beanFactory.getType(a);
	}

	public static <T> Object getBean(String a) throws BeansException {
		return beanFactory.getBean(a);
	}

	public static <T> T getBean(Class<T> a) throws BeansException {
		return beanFactory.getBean(a);
	}
}
