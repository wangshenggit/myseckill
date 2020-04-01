package com.seckill.dis.rmq.annotation;

import com.seckill.dis.rmq.constants.TopicEnum;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;


/**
 * 此注解用于标注消费者服务
 * .<br/>
 * 
 * Copyright: Copyright (c) 2017  zteits
 * 
 * @ClassName: MQConsumeService
 * @Description: 
 * @version: v1.0.0
 * @author: zhaowg
 * @date: 2018年3月2日 下午1:15:52
 * Modification History:
 * Date             Author          Version            Description
 *---------------------------------------------------------*
 * 2018年3月2日      zhaowg           v1.0.0               创建
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Service
public @interface MQConsumeService {
    /**
     * 消息主题
     */
     TopicEnum topic();

    /**
     * 消息标签,如果是该主题下所有的标签，使用“*”
     */
     String[] tags();


}
