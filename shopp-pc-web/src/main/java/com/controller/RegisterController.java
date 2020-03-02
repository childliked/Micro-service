package com.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.base.ResponseBase;
import com.constants.Constants;
import com.entity.UserEntity;
import com.feign.MemberServiceFegin;

@Controller
public class RegisterController {

	@Autowired
	private MemberServiceFegin memberServiceFegin;
	
	private static final String REGISTER = "register";
	private static final String LOGIN = "redirect:/login";

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registerGet() {

		return REGISTER;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerPost(UserEntity user, HttpServletRequest request) {

		// 1. 验证参数
		// 2. 调用会员注册接口
		ResponseBase regUser = memberServiceFegin.regUser(user);
		// 3. 如果失败，跳转到失败页面
		if(!regUser.getRtnCode().equals(Constants.HTTP_RES_CODE_200)) {
			request.setAttribute("error", "註冊失敗");
			 return REGISTER;
		}
		// 3. 如果成功，跳转到成功页面
		return LOGIN;
	}
}
