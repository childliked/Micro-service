package com.mq;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.adapter.MessageAdapter;
import com.alibaba.fastjson.JSONObject;
import com.constants.Constants;
import com.service.email.EmailService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ConsumerDistribute {

	@Autowired
	private EmailService emailService;
	private MessageAdapter messageAdapter;
	
	//监听消息
	@JmsListener(destination = "messages_queue")
	public void destribute(String json) {
		log.info("######消息服务平台接受到消息内容：{}#####",json);
		if(StringUtils.isEmpty(json)) {
			return ;
		}
		JSONObject RootJson = new JSONObject().parseObject(json);
		JSONObject HeaderJson = RootJson.getJSONObject("header");
		String interfaceType = HeaderJson.getString("interfaceType");
		
		if(StringUtils.isEmpty(interfaceType)) {
			return ;
		}
		//是否为发邮件
		if(interfaceType.equals(Constants.MSG_EMAIL)) {
			messageAdapter = emailService;
		}
		if(messageAdapter == null) {
			return ;
		}
		JSONObject contentJson = RootJson.getJSONObject("content");
		messageAdapter.sendMessage(contentJson);
	}
	
}
