package com.api.service.impl;

import java.util.Date;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.api.serivce.MemberService;
import com.base.BaseApiService;
import com.base.ResponseBase;
import com.constants.Constants;
import com.dao.MemberDao;
import com.entity.UserEntity;
import com.mq.RegisterMailboxProducer;
import com.utils.MD5Util;
import com.utils.TokenUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class MemberServiceImpl extends BaseApiService implements MemberService {

	@Autowired
	private MemberDao memberDao;
	@Autowired
	private RegisterMailboxProducer registerMailboxProducer;
	@Value("${messages.queue}")
	private String MESSAGESQUEUE;

	// 用户注册
	@Override
	public ResponseBase regUser(@RequestBody UserEntity user) {
		String password = user.getPassword();
		if (StringUtils.isEmpty(password)) {
			return setResultError("please input password....");
		}
		String NewPassword = MD5Util.MD5(password);
		user.setPassword(NewPassword);
		user.setCreated(new Date());
		user.setUpdated(new Date());
		Integer result = memberDao.insert(user);

		if (result <= 0) {
			setResultError("insert fail...");
		}

		// 采用mq异步发送消息
		String email = user.getEmail();
		String json = emailJson(email);
		log.info("####会员服务推送消息到消息服务平台####json:{}", json);
		sendMsg(json);

		return setResultSuccess("用户注册成功.");
	}

	// id找用户
	@Override
	public ResponseBase findUserById(Integer userId) {

		UserEntity user = memberDao.findByID(userId);
		if (user == null) {
			return setResultError("未查找到用户信息.");
		}
		return setResultSuccess(user);
	}

	// 推送消息
	private void sendMsg(String json) {

		ActiveMQQueue activeMQQueue = new ActiveMQQueue(MESSAGESQUEUE);
		registerMailboxProducer.sendMsg(activeMQQueue, json);

	}

	// 将email组装成json
	private String emailJson(String email) {

		JSONObject rootJson = new JSONObject();

		JSONObject header = new JSONObject();
		header.put("interfaceType", Constants.MSG_EMAIL);
		JSONObject content = new JSONObject();
		content.put("email", email);

		rootJson.put("header", header);
		rootJson.put("content", content);

		return rootJson.toJSONString();
	}

	// 用户登录
	@Override
	public ResponseBase login(@RequestBody UserEntity user) {

		// 1.验证参数
		String username = user.getUsername();
		if (StringUtils.isEmpty(username)) {
			return setResultError("用戶名称不能为空!");
		}
		String password = user.getPassword();
		if (StringUtils.isEmpty(password)) {
			return setResultError("密码不能为空!");
		}
		String newPassword = MD5Util.MD5(password);
		// 2.数据库校验
		UserEntity loginUser = memberDao.login(username, newPassword);
		// 3.自动登录
		return setLogin(loginUser);
	}

	// 找token登陆
	@Override
	public ResponseBase findByTokenUser(@RequestParam("token") String token) {

		// 1.验证参数
		if (StringUtils.isEmpty(token)) {
			return setResultError("token不能为空!");
		}
		// 2.从redis中 使用token 查找对应 userid
		String strUserId = (String) baseRedisService.getString(token);
		if (StringUtils.isEmpty(strUserId)) {
			return setResultError("token无效或者已经过期!");
		}
		// 3.使用userid数据库查询用户信息返回给客户端
		Integer userId = Integer.parseInt(strUserId);
		UserEntity userEntity = memberDao.findByID(userId);
		if (userEntity == null) {
			return setResultError("为查找到该用户信息");
		}
		userEntity.setPassword(null);
		return setResultSuccess(userEntity);
	}

	@Override
	public ResponseBase findByOpenId(@RequestParam("userOpenId") String userOpenId) {
		// 1.验证参数
		if (StringUtils.isEmpty(userOpenId)) {
			return setResultError("openid不能为空1");
		}
		// 2.使用openid 查询数据库 user表对应数据信息
		UserEntity userEntity = memberDao.findByOpenIdUser(userOpenId);
		if (userEntity == null) {
			return setResultError(Constants.HTTP_RES_CODE_201, "该openid没有关联");
		}
		// 3.自动登录
		return setLogin(userEntity);
	}

	// 自动登陆
	private ResponseBase setLogin(UserEntity userEntity) {
		if (userEntity == null) {
			return setResultError("账号或者密码不能正确");
		}
		// 3.将id和token存在redis中
		String memberToken = TokenUtil.getMemberToken();
		Integer userId = userEntity.getId();
		log.info("####用户信息token存放在redis中... key为:{},{}", memberToken, userId);
		baseRedisService.setString(memberToken, userId + "", Constants.REDIS_TIME);
		// 4.返回token
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("memberToken", memberToken);
		return setResultSuccess(jsonObject);
	}

	@Override
	public ResponseBase qqLogin(@RequestBody UserEntity user) {
		// 1.验证参数
		String openId = user.getOpenId();
		if (StringUtils.isEmpty(openId)) {
			return setResultError("openid不能为空!");
		}
		// 2.先进行账号登录
		ResponseBase setLogin = login(user);
		if (!setLogin.getRtnCode().equals(Constants.HTTP_RES_CODE_200)) {
			return setLogin;
		}
		//登陆成功后修改用户的openId字段
		JSONObject data = (JSONObject) setLogin.getData();
		String memberToken = (String) data.get("memberToken");
		Integer userId = (Integer)baseRedisService.getString(memberToken);
		Integer result = memberDao.updateByOpenIdUser(openId, userId);
		if (result <= 0) {
			return setResultError("QQ账号关联失败失败!");
		}
		return setLogin;
	}

}
