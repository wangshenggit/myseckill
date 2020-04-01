package com.seckill.dis.rmq.producer;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

/**
 * 判断是否需要适合消息生产者配置
 * .<br/>
 * 
 * Copyright: Copyright (c) 2017  zteits
 * 
 * @ClassName: MQProducerCondition
 * @Description: 
 * @version: v1.0.0
 * @author: zhaowg
 * @date: 2018年3月1日 上午11:22:56
 * Modification History:
 * Date             Author          Version            Description
 *---------------------------------------------------------*
 * 2018年3月1日      zhaowg           v1.0.0               创建
 */
public class MQProducerCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		//判断当前环境开关是否开启
		String isOnOff = context.getEnvironment().getProperty("rocketmq.producer.isOnOff");
		//当且仅当值为on时，返回true
		if(!StringUtils.isEmpty(isOnOff) && isOnOff.equalsIgnoreCase("on")){
			return true;
		}
		return false;
	}

}
