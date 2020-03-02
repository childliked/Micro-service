package com.service.email;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.adapter.MessageAdapter;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService implements MessageAdapter {

	@Autowired
	private JavaMailSender javaMailSender;
	@Value("${msg.subject}")
	private String subject;
	@Value("${msg.text}")
	private String text;
	
	@Override
	public void sendMessage(JSONObject body) {
		
		// 处理发送邮件
		String email = body.getString("email");
		if (StringUtils.isEmpty(email)) {
			return;
		}
		log.info("消息服务平台发送邮件:{}开始", email);
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(email);
		simpleMailMessage.setTo(email);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(text.replace("{}", email));
		javaMailSender.send(simpleMailMessage);
		log.info("消息服务平台发送邮件:{}完成", email);
		
	}

}
