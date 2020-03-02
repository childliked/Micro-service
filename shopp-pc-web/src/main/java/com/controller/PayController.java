package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.base.ResponseBase;
import com.constants.Constants;
import com.feign.PayServiceFegin;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class PayController {

	@Autowired
	private PayServiceFegin payServiceFegin;
	
	@RequestMapping("/aliPay")
	public void aliPay(String payToken, HttpServletResponse response) throws IOException {
		
		PrintWriter writer = response.getWriter();
		response.setContentType("text/html;charset=utf-8");
		//1.验证参数
		if (StringUtils.isEmpty(payToken)) {
			return;
		}
		//2.得到from表单元素
		ResponseBase payTokenResult = payServiceFegin.findPayToken(payToken);
		if (!payTokenResult.getRtnCode().equals(Constants.HTTP_RES_CODE_200)) {
			String msg = payTokenResult.getMsg();
			writer.println(msg);
			return;
		}
		LinkedHashMap data = (LinkedHashMap) payTokenResult.getData();
		String payHtml = (String) data.get("payHtml");
		//3.提交给客户端运行
		writer.println(payHtml);
		
		log.info("####PayController###payHtml:{}",payHtml);
		writer.close();
	}
}
