package com.api.serivce;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.base.ResponseBase;
import com.entity.UserEntity;

@RequestMapping("/member")
public interface MemberService {

	// 注册用户
	@RequestMapping("/regUser")
	ResponseBase regUser(@RequestBody UserEntity user);

	// 查找用户
	@RequestMapping("/findUserById")
	ResponseBase findUserById(Integer userId);

	// 用户登陆
	@RequestMapping("/login")
	ResponseBase login(@RequestBody UserEntity user);

	// 使用token进行登录
	@RequestMapping("/findByTokenUser")
	ResponseBase findByTokenUser(@RequestParam("token") String token);
	
	// 使用token进行登录
	@RequestMapping("/findByOpenId")
	ResponseBase findByOpenId(@RequestParam("userOpenId") String userOpenId);
	
	// qq联合登陆绑定用户
	@RequestMapping("/qqLogin")
	ResponseBase qqLogin(@RequestBody UserEntity user);
}
