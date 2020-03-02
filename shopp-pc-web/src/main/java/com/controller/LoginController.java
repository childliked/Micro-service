package com.controller;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.base.ResponseBase;
import com.constants.Constants;
import com.entity.UserEntity;
import com.feign.MemberServiceFegin;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.oauth.Oauth;
import com.utils.CookieUtil;

@Controller
public class LoginController {

	@Autowired
	private MemberServiceFegin memberServiceFegin;

	private static final String LOGIN = "LOGIN";
	private static final String ERROR = "error";
	private static final String INDEX = "redirect:/index";
	private static final String QQRELATION = "qqrelation";

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginGet() {
		return LOGIN;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginPost(UserEntity user, HttpServletResponse response, HttpServletRequest request) {
		// 1.验证参数
		// 2.调用登录接口，获取token信息
		ResponseBase loginBase = memberServiceFegin.login(user);
		if (!loginBase.getRtnCode().equals(Constants.HTTP_RES_CODE_200)) {
			request.setAttribute("error", "账号或者密码错误!");
			return LOGIN;
		}
		// 3.获取token信息存放在cookie里面
		LinkedHashMap loginData = (LinkedHashMap) loginBase.getData();
		String memberToken = (String) loginData.get("memberToken");
		setCookie(memberToken, response);
		return INDEX;
	}

	// 存放cookie
	public void setCookie(String memberToken, HttpServletResponse response) {
		CookieUtil.addCookie(response, Constants.COOKIE_MEMBER_TOKEN, memberToken, Constants.COOKIE_TOKEN_MEMBER_TIME);
	}

	// QQ联合登陆
	// 生成qq授权登录链接
	@RequestMapping("/locaQQLogin")
	public String locaQQLogin(HttpServletRequest reqest) throws QQConnectException {
		String authorizeURL = new Oauth().getAuthorizeURL(reqest);
		return "redirect:" + authorizeURL;
	}

	// qq登陆查看是否绑定账号
	@RequestMapping("/qqLoginCallback")
	public String qqLoginCallback(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession)
			throws QQConnectException {
		// 1.获取授权码COde
		// 2.使用授权码Code获取accessToken
		AccessToken accessTokenOj = new Oauth().getAccessTokenByRequest(request);
		if (accessTokenOj == null) {
			request.setAttribute("error", "QQ授权失败");
			return ERROR;
		}
		String accessToken = accessTokenOj.getAccessToken();
		if (accessToken == null) {
			request.setAttribute("error", "accessToken为null");
			return ERROR;
		}
		// 3.使用AccessToken换取openId
		OpenID openidOj = new OpenID(accessToken);
		String userOpenId = openidOj.getUserOpenID();
		// 4.查看数据库该id是否绑定用户
		ResponseBase openUserBase = memberServiceFegin.findByOpenId(userOpenId);
		if (!openUserBase.getRtnCode().equals(Constants.HTTP_RES_CODE_200)) {
			httpSession.setAttribute("qqOpenid", userOpenId);
			return QQRELATION;
		}
		// 6.已经绑定账号 自动登录 将用户token信息存放在cookie中
		LinkedHashMap dataTokenMap = (LinkedHashMap) openUserBase.getData();
		String memberToken = (String) dataTokenMap.get("memberToken");
		setCookie(memberToken, response);
		return INDEX;
	}

	// 关联账户
	@RequestMapping(value = "/qqRelation", method = RequestMethod.POST)
	public String qqRelation(UserEntity user, HttpSession httpSession, HttpServletRequest request, HttpServletResponse response) {
		// 1.获取openid
		String qqOpenid = (String) httpSession.getAttribute("qqOpenid");
		if (StringUtils.isEmpty(qqOpenid)) {
			request.setAttribute("error", "没有获取到openid");
			return ERROR;
		}
		// 2.调用绑定用户接口
		ResponseBase qqLogin = memberServiceFegin.qqLogin(user);
		if (!qqLogin.getRtnCode().equals(Constants.HTTP_RES_CODE_200)) {
			request.setAttribute("error", "账号或者密码错误!");
			return LOGIN;
		}
		// 3.将token信息存放在cookie里面
		LinkedHashMap loginData = (LinkedHashMap) qqLogin.getData();
		String memberToken = (String) loginData.get("memberToken");
		if (StringUtils.isEmpty(memberToken)) {
			request.setAttribute("error", "会话已经失效!");
			return LOGIN;
		}
		setCookie(memberToken, response);
		return INDEX;
	}
}
