package com.rkylin.multigates.utils;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.google.common.collect.Maps;
import com.rkylin.gateway.pojo.BusiSysInfo;

import java.lang.reflect.Method;
import java.util.Map;

public class DubboUtil2 {
	
	private static Map<String,ReferenceConfig> cache = Maps.newHashMap();

	public static Object sendMessage(BusiSysInfo bsi,Object msg,Class cl) throws Exception{
		String key = bsi.getDubboUrl()+bsi.getDubboGroup()+bsi.getDubboApi()+bsi.getDubboMethod()+bsi.getDubboVersion();
		ReferenceConfig referenceConfig = cache.get(key);

		//如果没有之前的连接，就创建一个加入缓存中
		if(referenceConfig==null){
			referenceConfig = createReferenceConfig(bsi);
			cache.put(key, referenceConfig);
		}

		Object targetInterface = referenceConfig.get();
		Method method = targetInterface.getClass().getMethod(bsi.getDubboMethod(),cl);
		Object returnObject = method.invoke(targetInterface, msg);
		return returnObject ;
	}


	private static ReferenceConfig createReferenceConfig(BusiSysInfo bsi) throws ClassNotFoundException{

		// 当前应用配置
		ApplicationConfig application = new ApplicationConfig();
		application.setName("DubboMsgCenter");

		// 连接注册中心配置
		RegistryConfig registry = new RegistryConfig();
		registry.setAddress(bsi.getDubboUrl());
		registry.setProtocol("zookeeper");

		// 注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接
		String dubboApi = bsi.getDubboApi();

		// 引用远程服务
		ReferenceConfig reference = new ReferenceConfig(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
		reference.setApplication(application);
		reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
		reference.setInterface(dubboApi);
		reference.setGroup(bsi.getDubboGroup());
		reference.setVersion(bsi.getDubboVersion());
		reference.setRetries(0);
		reference.setTimeout(30000);
//
//
//		// 和本地bean一样使用xxxService
//		XxxService xxxService = reference.get(); // 注意：此代理对象内部封装了所有通讯细节，对象较重，请缓存复用

		return reference;
	}
}
